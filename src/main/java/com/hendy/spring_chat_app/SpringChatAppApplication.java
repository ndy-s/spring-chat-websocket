package com.hendy.spring_chat_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringChatAppApplication {

    public static void main(String[] args) {
        // Dotenv dotenv = Dotenv.configure().load();
        // dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(),
        // entry.getValue()));

        SpringApplication.run(SpringChatAppApplication.class, args);
    }

}
