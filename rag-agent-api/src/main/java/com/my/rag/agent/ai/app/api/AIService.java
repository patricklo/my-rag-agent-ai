package com.my.rag.agent.ai.app.api;

import org.springframework.ai.chat.ChatResponse;
import reactor.core.publisher.Flux;

public interface AIService {
    ChatResponse generate(String model, String message);

    Flux<ChatResponse> generateStream(String model, String message);
}
