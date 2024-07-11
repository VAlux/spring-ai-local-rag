package dev.alvo.rag.document.conversion;

import dev.langchain4j.data.segment.TextSegment;
import org.springframework.ai.document.Document;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class TextSegmentToSpringDocumentConverter implements Converter<TextSegment, Document> {

  @Override
  public Document convert(TextSegment source) {
    return new Document(source.text(), source.metadata().toMap());
  }
}
