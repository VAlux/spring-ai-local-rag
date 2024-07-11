package dev.alvo.rag.document.split;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.segment.TextSegment;

import java.util.List;

public abstract class DocumentSplittingService<T extends DocumentSplitter> {

  protected final T splitter;

  protected DocumentSplittingService(T splitter) {
    this.splitter = splitter;
  }

  public List<TextSegment> split(Document document) {
    return this.splitter.split(document);
  }

  public List<TextSegment> splitAll(List<Document> documents) {
    return this.splitter.splitAll(documents);
  }
}
