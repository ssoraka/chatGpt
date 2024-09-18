package ru.openai.gpt.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.openai.gpt.config.GptProps;
import ru.openai.gpt.entity.MessageEntity;
import ru.openai.gpt.entity.UserEntity;
import ru.openai.gpt.model.assistant.AssistantV2;
import ru.openai.gpt.model.messages.MessageRequestV2;
import ru.openai.gpt.model.messages.MessageV2;
import ru.openai.gpt.model.runs.RunCreateRequestV2;
import ru.openai.gpt.model.runs.RunV2;
import ru.openai.gpt.model.thread.ThreadRequestV2;
import ru.openai.gpt.model.thread.ThreadV2;
import ru.openai.gpt.repository.MessageRepository;
import ru.openai.gpt.repository.UserRepository;
import ru.openai.gpt.service.telegram.TelegramBotService;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class BusinessService {
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final MyOpenAiService openAiService;
    private final GptProps gptProps;
    private final TelegramBotService telegramBotService;

    @Transactional
    public void sendMessageAndReceiveAnswer(UserEntity user, MessageEntity messageEntity) {

        AssistantV2 assistant = openAiService.retrieveAssistant(gptProps.getAssistantId());
        System.out.println(assistant);

        MessageRequestV2 message = MessageRequestV2.builder()
                .role("user")
                .content(messageEntity.getRequest())
                .build();

        if (Objects.isNull(user.getThreadId())) {
            ThreadV2 thread = openAiService.createThread(ThreadRequestV2.builder()
                .build());
            user.setThreadId(thread.getId());
            userRepository.save(user);
        }

        RunCreateRequestV2 runRequest = RunCreateRequestV2.builder()
                .assistantId(assistant.getId())
                .toolChoice("{\"type\":\"file_search\"}")
                .build();
        RunV2 run = openAiService.createRun(user.getThreadId(), runRequest);

        while (!run.getStatus().equals("completed")) {

            try {
                Thread.sleep(1000l);
            } catch (Exception ignored) {}

            run = openAiService.retrieveRun(user.getThreadId(), run.getId());

            if (List.of("failed", "cancelled", "expired").contains(run.getStatus())) {
                log.error("пришел ответ со статусом " + run.getStatus());
                telegramBotService.editMarkup(user.getTelegramId(), messageEntity.getTelegramMessageId(), "пришел ответ со статусом " + run.getStatus());
                return;
            }
        }

        MessageV2 messageV2 = openAiService.retrieveMessages(user.getThreadId()).getData().get(0);
        String text = messageV2.getContent().get(0).getText().getValue();
        messageEntity.setResponse(text);
        messageRepository.save(messageEntity);

        telegramBotService.editMarkup(user.getTelegramId(), messageEntity.getTelegramMessageId(), text);
    }
}
