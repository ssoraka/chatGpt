package ru.openai.gpt.config;

import com.theokanning.openai.service.OpenAiService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.openai.gpt.service.MyOpenAiService;

@Configuration
@EnableConfigurationProperties(GptProps.class)
@RequiredArgsConstructor
public class Config {

    private final GptProps gptProps;

    @Bean
    public MyOpenAiService myOpenAiService() {
        MyOpenAiService openAiService = new MyOpenAiService(gptProps);
        return openAiService;
    }

    @Bean
    public OpenAiService openAiService() {
        return new OpenAiService(gptProps.getToken());
    }
}
