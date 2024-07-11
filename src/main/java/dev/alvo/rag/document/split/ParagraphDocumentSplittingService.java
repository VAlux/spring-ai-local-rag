package dev.alvo.rag.document.split;

import dev.langchain4j.data.document.splitter.DocumentByParagraphSplitter;
import org.springframework.stereotype.Service;

@Service
public final class ParagraphDocumentSplittingService extends DocumentSplittingService<DocumentByParagraphSplitter> {
  private ParagraphDocumentSplittingService() {
    super(new DocumentByParagraphSplitter(512, 10));
  }
}
