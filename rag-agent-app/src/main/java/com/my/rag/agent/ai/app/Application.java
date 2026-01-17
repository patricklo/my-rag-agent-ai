package com.my.rag.agent.ai.app;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@Configurable
@ComponentScan(basePackages = "com.my.rag.agent.ai")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }
}
