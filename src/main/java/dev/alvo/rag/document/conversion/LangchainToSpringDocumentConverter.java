package dev.alvo.rag.document.conversion;

import dev.langchain4j.data.document.Document;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class LangchainToSpringDocumentConverter implements Converter<Document, org.springframework.ai.document.Document> {

  @Override
  public org.springframework.ai.document.Document convert(Document source) {
    return new org.springframework.ai.document.Document(source.text(), source.metadata().toMap());
  }
}
