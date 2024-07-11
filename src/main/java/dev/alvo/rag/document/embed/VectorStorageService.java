package dev.alvo.rag.document.embed;

import dev.alvo.rag.document.conversion.TextSegmentToSpringDocumentConverter;
import dev.langchain4j.data.segment.TextSegment;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class VectorStorageService {

  private final VectorStore vectorStore;
  private final TextSegmentToSpringDocumentConverter textSegmentConverter;

  @Autowired
  public VectorStorageService(VectorStore vectorStore, TextSegmentToSpringDocumentConverter textSegmentConverter) {
    this.vectorStore = vectorStore;
    this.textSegmentConverter = textSegmentConverter;
  }

  public List<Document> search(String query, Integer topK) {
    return this.vectorStore.similaritySearch(SearchRequest.query(query).withTopK(topK));
  }

  public void persistDocument(Document document) {
    this.vectorStore.add(List.of(document));
  }

  public void persistDocuments(List<Document> documents) {
    this.vectorStore.add(documents);
  }

  public void persistSegment(TextSegment segment) {
    this.vectorStore.add(List.of(Objects.requireNonNull(this.textSegmentConverter.convert(segment))));
  }

  public void persistSegments(List<TextSegment> segments) {
    this.vectorStore.add(segments.stream().map(this.textSegmentConverter::convert).toList());
  }
}
