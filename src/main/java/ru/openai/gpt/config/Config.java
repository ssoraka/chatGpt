package ru.openai.gpt.config;

import com.theokanning.openai.service.OpenAiService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(GptProps.class)
@RequiredArgsConstructor
public class Config {

    private final GptProps gptProps;

    @Bean
    public OpenAiService openAiService() {
        System.out.println("токен: " + gptProps.getToken());
        return new OpenAiService(gptProps.getToken());
    }
}
