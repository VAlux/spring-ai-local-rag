package dev.alvo.rag.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "rag")
public class RagConfigurationProperties {
  private Float temperature;
  private Integer topK;
  private String systemPrompt;

  public Float getTemperature() {
    return this.temperature;
  }

  public void setTemperature(Float temperature) {
    this.temperature = temperature;
  }

  public Integer getTopK() {
    return this.topK;
  }

  public void setTopK(Integer topK) {
    this.topK = topK;
  }

  public String getSystemPrompt() {
    return this.systemPrompt;
  }

  public void setSystemPrompt(String systemPrompt) {
    this.systemPrompt = systemPrompt;
  }
}
