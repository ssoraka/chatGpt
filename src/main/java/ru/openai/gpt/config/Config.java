package ru.openai.gpt.config;

import com.theokanning.openai.assistants.Assistant;
import com.theokanning.openai.messages.MessageRequest;
import com.theokanning.openai.runs.Run;
import com.theokanning.openai.runs.RunCreateRequest;
import com.theokanning.openai.service.OpenAiService;
import com.theokanning.openai.threads.Thread;
import com.theokanning.openai.threads.ThreadRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import ru.openai.gpt.model.assistant.AssistantV2;
import ru.openai.gpt.service.MyOpenAiService;

import java.util.List;

@Configuration
@EnableConfigurationProperties(GptProps.class)
@RequiredArgsConstructor
public class Config {

    private final GptProps gptProps;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public MyOpenAiService openAiService() {
        MyOpenAiService openAiService = new MyOpenAiService(gptProps);
        AssistantV2 assistant = openAiService.retrieveAssistant(gptProps.getAssistantId());
        System.out.println(assistant);

//        MessageRequest message = MessageRequest.builder()
//                .role("user")
//                .content("Привет, как тебя зовут ? ")
//                .build();
//
//        Thread thread = openAiService.createThread(ThreadRequest.builder()
//                .messages(List.of(message))
//                .build());
//
//        RunCreateRequest runRequest = RunCreateRequest.builder()
//                .assistantId(assistant.getId())
//                .build();
//        Run run = openAiService.createRun(thread.getId(), runRequest);

        return openAiService;
    }
}
