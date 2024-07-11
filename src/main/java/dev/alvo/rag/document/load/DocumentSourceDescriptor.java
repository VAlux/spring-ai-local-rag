package dev.alvo.rag.document.load;

import dev.langchain4j.data.document.DocumentParser;

import java.nio.file.PathMatcher;

public sealed interface DocumentSourceDescriptor {
  //@formatter:off
  record FileSystemDocumentSource(String rootDirectory,
                                  PathMatcher pathMatcher,
                                  DocumentParser parser) implements DocumentSourceDescriptor {}

  record GithubDocumentSource(String owner,
                              String repo,
                              String branch,
                              String extensionFilter,
                              DocumentParser parser) implements DocumentSourceDescriptor {}
  //@formatter:on
}
