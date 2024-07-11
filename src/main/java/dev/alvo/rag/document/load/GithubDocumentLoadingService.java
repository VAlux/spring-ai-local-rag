package dev.alvo.rag.document.load;

import dev.alvo.rag.document.load.DocumentSourceDescriptor.GithubDocumentSource;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.github.GitHubDocumentLoader;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GithubDocumentLoadingService implements DocumentLoadingService<GithubDocumentSource> {

  private static final Logger LOGGER = LoggerFactory.getLogger(GithubDocumentLoadingService.class);

  @Value("${github-token}")
  private String token;

  private GitHubDocumentLoader loader;

  @PostConstruct
  public void initLoader() {
    this.loader = GitHubDocumentLoader.builder()
      .gitHubToken(this.token)
      .build();
  }

  @Override
  public List<Document> loadDocuments(DocumentSourceDescriptor source) {
    var githubSource = (GithubDocumentSource) source;

    var documents = this.loader.loadDocuments(
      githubSource.owner(),
      githubSource.repo(),
      githubSource.branch(),
      githubSource.parser());

    var filteredDocuments = documents.stream()
      .filter(document -> document.metadata().getString("github_file_name").endsWith(".java"))
      .toList();

    LOGGER.info("Loaded {} documents from the github repo: {}", filteredDocuments.size(), githubSource.repo());
    return filteredDocuments;
  }

  @Override
  public Class<GithubDocumentSource> getDescriptorType() {
    return GithubDocumentSource.class;
  }
}
