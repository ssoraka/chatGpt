package ru.openai.gpt.config;

import com.theokanning.openai.service.OpenAiService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    @Value("${openai.chatgpt.token}")
    private String token;

    @Bean
    public OpenAiService openAiService() {
        System.out.println("токен: " + token);
        return new OpenAiService(token);
    }
}
