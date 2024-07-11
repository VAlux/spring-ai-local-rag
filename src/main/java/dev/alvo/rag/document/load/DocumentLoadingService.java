package dev.alvo.rag.document.load;

import dev.langchain4j.data.document.Document;

import java.util.List;

public interface DocumentLoadingService<T extends DocumentSourceDescriptor> {
  List<Document> loadDocuments(DocumentSourceDescriptor source);

  Class<T> getDescriptorType();
}
