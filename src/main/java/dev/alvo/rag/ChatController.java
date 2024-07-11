package dev.alvo.rag;

import dev.alvo.rag.document.load.DocumentSourceDescriptor.FileSystemDocumentSource;
import dev.alvo.rag.document.load.DocumentSourceDescriptor.GithubDocumentSource;
import dev.langchain4j.data.document.parser.TextDocumentParser;
import org.springframework.ai.ollama.api.OllamaApi.ChatResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.Charset;
import java.nio.file.FileSystems;

@RestController
class ChatController {

  //@Formatter:off
  record FilesystemPrompt(String content, String rootDirectory, String extensionFilter) { }
  record GithubPrompt(String content, String owner, String repo, String branch, String extensionFilter) { }
  //@Formatter:on

  private final OllamaChatService ollamaChatService;

  @Autowired
  public ChatController(OllamaChatService ollamaChatService) {
    this.ollamaChatService = ollamaChatService;
  }

  @PostMapping("/rag/filesystem")
  public ChatResponse filesystemRag(@RequestBody FilesystemPrompt prompt) {
    var source = new FileSystemDocumentSource(
      prompt.rootDirectory,
      FileSystems.getDefault().getPathMatcher("glob:**." + prompt.extensionFilter),
      new TextDocumentParser(Charset.defaultCharset())
    );

    return this.ollamaChatService.ragCompletion(source, prompt.content);
  }

  @PostMapping("/rag/github")
  public ChatResponse githubRag(@RequestBody GithubPrompt prompt) {
    var source = new GithubDocumentSource(
      prompt.owner,
      prompt.repo,
      prompt.branch,
      prompt.extensionFilter,
      new TextDocumentParser(Charset.defaultCharset())
    );

    return this.ollamaChatService.ragCompletion(source, prompt.content);
  }
}
