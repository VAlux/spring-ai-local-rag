package dev.alvo.rag.document.embed;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.segment.TextSegment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocumentEmbeddingService {

  private static final Logger LOGGER = LoggerFactory.getLogger(DocumentEmbeddingService.class);

  private final EmbeddingModel embeddingModel;

  @Autowired
  public DocumentEmbeddingService(EmbeddingModel embeddingModel) {
    this.embeddingModel = embeddingModel;
  }

  public EmbeddingResponse embedSegments(List<TextSegment> segments) {
    var segmentsContent = segments.stream().map(TextSegment::text).toList();
    LOGGER.info("Embedding {} text segments", segmentsContent.size());
    return this.embeddingModel.embedForResponse(segmentsContent);
  }

  public EmbeddingResponse embedDocuments(List<Document> documents) {
    var documentsContent = documents.stream().map(Document::text).toList();
    LOGGER.info("Embedding {} documents", documentsContent.size());
    return this.embeddingModel.embedForResponse(documentsContent);
  }
}
