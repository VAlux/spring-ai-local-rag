package dev.alvo.rag.document.load;

import dev.alvo.rag.document.load.DocumentSourceDescriptor.FileSystemDocumentSource;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FilesystemDocumentLoadingService implements DocumentLoadingService<FileSystemDocumentSource> {

  private static final Logger LOGGER = LoggerFactory.getLogger(FilesystemDocumentLoadingService.class);

  @Override
  public List<Document> loadDocuments(DocumentSourceDescriptor source) {
    var fileSystemSource = ((FileSystemDocumentSource) source);

    final var documents = FileSystemDocumentLoader.loadDocumentsRecursively(
      fileSystemSource.rootDirectory(),
      fileSystemSource.pathMatcher(),
      fileSystemSource.parser());

    LOGGER.info("Loaded {} documents from the filesystem at {}", documents.size(), fileSystemSource.rootDirectory());
    return documents;
  }

  @Override
  public Class<FileSystemDocumentSource> getDescriptorType() {
    return FileSystemDocumentSource.class;
  }
}
