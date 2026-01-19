package com.my.rag.agent.ai.app.api;

import com.my.rag.agent.ai.app.api.response.Response;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface RAGService {
    Response<List<String>> queryRagTagList();
    Response<String> uploadFile(String ragTag, List<MultipartFile> files);

    Response<String> analyzeGitRepository(String repoUrl, String username, String token) throws Exception;
}
