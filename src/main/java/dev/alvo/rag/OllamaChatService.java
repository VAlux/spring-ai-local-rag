package dev.alvo.rag;

import dev.alvo.rag.config.properties.RagConfigurationProperties;
import dev.alvo.rag.document.embed.VectorStorageService;
import dev.alvo.rag.document.load.DocumentLoadingService;
import dev.alvo.rag.document.load.DocumentSourceDescriptor;
import dev.alvo.rag.document.split.ParagraphDocumentSplittingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.ollama.api.OllamaApi.ChatRequest;
import org.springframework.ai.ollama.api.OllamaApi.ChatResponse;
import org.springframework.ai.ollama.api.OllamaApi.Message;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class OllamaChatService {

  private static final Logger LOGGER = LoggerFactory.getLogger(OllamaChatService.class);

  private final RestTemplate restTemplate;
  private final Map<Class<? extends DocumentSourceDescriptor>, DocumentLoadingService<?>> documentLoadingServices;
  private final ParagraphDocumentSplittingService splittingService;
  private final VectorStorageService vectorStorageService;
  private final RagConfigurationProperties ragProperties;

  @Value("${spring.ai.ollama.chat.model}")
  private String ollamaModel;

  @Value("${spring.ai.ollama.base-url}")
  private String ollamaBaseUrl;

  @Autowired
  public OllamaChatService(RestTemplate restTemplate,
                           List<DocumentLoadingService<?>> documentLoadingServices,
                           ParagraphDocumentSplittingService splittingService,
                           VectorStorageService vectorStorageService,
                           RagConfigurationProperties ragConfigurationProperties) {

    this.restTemplate = restTemplate;
    this.splittingService = splittingService;
    this.vectorStorageService = vectorStorageService;
    this.ragProperties = ragConfigurationProperties;

    this.documentLoadingServices = documentLoadingServices.stream()
      .collect(Collectors.toMap(DocumentLoadingService::getDescriptorType, Function.identity()));
  }

  public ChatResponse ragCompletion(DocumentSourceDescriptor contextSource, String userPrompt) {
    var documents = this.documentLoadingServices.get(contextSource.getClass()).loadDocuments(contextSource);
    var documentSegments = this.splittingService.splitAll(documents);
    this.vectorStorageService.persistSegments(documentSegments);

    var context = this.vectorStorageService.search(userPrompt, this.ragProperties.getTopK()).stream()
      .map(Document::getContent)
      .collect(Collectors.joining("\n"));

    LOGGER.info("Pulled context for the user prompt: {}", context);

    var messages = List.of(
      Message.builder(Message.Role.SYSTEM).withContent(this.ragProperties.getSystemPrompt()).build(),
      Message.builder(Message.Role.USER).withContent("Relevant context: " + context).build(),
      Message.builder(Message.Role.USER).withContent(userPrompt).build()
    );

    var request = ChatRequest.builder(this.ollamaModel)
      .withStream(false)
      .withMessages(messages)
      .withOptions(OllamaOptions.create().withTemperature(this.ragProperties.getTemperature()))
      .build();

    LOGGER.info("sending a request to ollama {}", request);

    var response =
      this.restTemplate.postForEntity(this.ollamaBaseUrl + "/api/chat", request, ChatResponse.class);

    LOGGER.info("model {} responded with the following message: {}",
      response.getBody().model(), response.getBody().message().content());

    return response.getBody();
  }
}
