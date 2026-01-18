package com.my.rag.agent.ai.app.test;

import com.alibaba.fastjson.JSON;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.ollama.OllamaChatClient;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.PgVectorStore;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class RAGTest {

    @Resource
    private OllamaChatClient ollamaChatClient;

    @Resource
    private TokenTextSplitter tokenTextSplitter;

    @Resource
    private SimpleVectorStore simpleVectorStore;

    @Resource
    private PgVectorStore pgVectorStore;

    @Test
    public void upload(){
        TikaDocumentReader reader = new TikaDocumentReader("./data/file.text");
        List<Document> documents = reader.get();
        List<Document> documentSplitterList = tokenTextSplitter.apply(documents);
        documents.forEach(doc -> doc.getMetadata().put("knowledge","知识库的名称"));
        documentSplitterList.forEach(doc -> doc.getMetadata().put("knowledge","知识库的名称"));
        pgVectorStore.accept(documentSplitterList);

        log.info("upload successfully");
    }


    //上传完知识库，直接测试对话
    @Test
    public void chat(){
        String message = "王大瓜，哪年出生";
        String SYSTREM_PROMPT = """
                Use the information from the DOCUMENTS section to provide accurate answers but act as if you knew this information.
                If unsure, simple state that you don't know
                Another thing you need to note is that your reply must be in Chinese!
                DOCUMENTS:
                    {documents}
                """;
        SearchRequest requests = SearchRequest.query(message).withTopK(5).withFilterExpression("knowledge == '知识库的名称'");
        List<Document> documents = pgVectorStore.similaritySearch(requests);
        String documentsCollectors = documents.stream().map(Document::getContent).collect(Collectors.joining());
        Message ragMessage = new SystemPromptTemplate(SYSTREM_PROMPT).createMessage(Map.of("documents", documentsCollectors));

        List<Message> messages = new ArrayList<>();
        messages.add(new UserMessage(message));
        messages.add(ragMessage);

        ChatResponse chatResponse = ollamaChatClient.call(new Prompt(messages, OllamaOptions.create().withModel("deepseek-r1:1.5b")));
        log.info("test result:{}", JSON.toJSONString(chatResponse));

    }
}
