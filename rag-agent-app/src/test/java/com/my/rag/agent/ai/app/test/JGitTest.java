package com.my.rag.agent.ai.app.test;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.ai.document.Document;
import org.springframework.ai.ollama.OllamaChatClient;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.PgVectorStore;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.PathResource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class JGitTest {

    @Resource
    private OllamaChatClient ollamaChatClient;

    @Resource
    private TokenTextSplitter tokenTextSplitter;

    @Resource
    private SimpleVectorStore simpleVectorStore;

    @Resource
    private PgVectorStore pgVectorStore;

    @Test
    public void test() throws IOException, GitAPIException {
        String repoURL = "https://gitcode.net/KnowledgePlanet/ai-mcp-gateway";
        String username = "seasontbf";
        String password = "xEyrKZNMRy_kz8p9yUfQ";

        String localPath = "./cloned-repo";

        log.info("clone path:{}", new File(localPath).getAbsolutePath());

        FileUtils.deleteDirectory(new File(localPath));

        Git git = Git.cloneRepository().setURI(repoURL)
                .setDirectory(new File(localPath))
                .setCredentialsProvider(new UsernamePasswordCredentialsProvider(username,password))
                .call();

        git.close();
    }

    @Test
    public void testFile() throws IOException {
        Files.walkFileTree(Paths.get("cloned-repo"), new SimpleFileVisitor<>(){
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                log.info("file path:{}", file.toString());
                PathResource resource = new PathResource(file);

//                TikaDocumentReader reader = new TikaDocumentReader(resource);
//                List<Document> documents = reader.get();
//                List<Document> documentSplitterList = tokenTextSplitter.apply(documents);
//                documents.forEach(doc -> doc.getMetadata().put("knowledge","ai-mcp-gateway"));
//                documentSplitterList.forEach(doc -> doc.getMetadata().put("knowledge","ai-mcp-gateway"));
//                pgVectorStore.accept(documentSplitterList);

                return super.visitFile(file, attrs);
            }
        });
    }
}
