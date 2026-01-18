package com.my.rag.agent.ai.app.config;

import org.springframework.ai.ollama.OllamaChatClient;
import org.springframework.ai.ollama.OllamaEmbeddingClient;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.PgVectorStore;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class OllamaConfig {

    @Bean
    public OllamaApi ollamaApi(@Value("${spring.ai.ollama.base-url}") String baseUrl) {
        return new OllamaApi(baseUrl);
    }

    @Bean
    public OllamaChatClient ollamaChatClient(OllamaApi ollamaApi) {
        return new OllamaChatClient(ollamaApi);
    }

    @Bean
    public TokenTextSplitter tokenTextSplitter() {
        return new TokenTextSplitter();
    }

    /**
     * Option 1. SimpleVectorStore which won't store into PG Vector
     */
    @Bean
    public SimpleVectorStore vectorStore(OllamaApi ollamaApi) {
        OllamaEmbeddingClient embeddingClient = new OllamaEmbeddingClient(ollamaApi);
        embeddingClient.withDefaultOptions(OllamaOptions.create().withModel("nomic-embed-text"));//也可以配置
    // ai:
//        embedding:
//           options:
//           num-batch: 512
//        model: nomic-embed-text
        return new SimpleVectorStore(embeddingClient);
    }

    /**
     * Option 2. PgVectorStore which will store into PG Vector
     */
    @Bean
    public PgVectorStore pgVectorStore(OllamaApi ollamaApi, JdbcTemplate jdbcTemplate) {
        OllamaEmbeddingClient embeddingClient = new OllamaEmbeddingClient(ollamaApi);
        embeddingClient.withDefaultOptions(OllamaOptions.create().withModel("nomic-embed-text"));
        return new PgVectorStore(jdbcTemplate, embeddingClient);
    }
}
