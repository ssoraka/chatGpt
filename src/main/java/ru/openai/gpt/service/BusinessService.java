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
import ru.openai.gpt.model.runSteps.RunStepV2;
import ru.openai.gpt.model.runs.RunCreateRequestV2;
import ru.openai.gpt.model.runs.RunV2;
import ru.openai.gpt.model.thread.ThreadRequestV2;
import ru.openai.gpt.model.thread.ThreadV2;
import ru.openai.gpt.repository.MessageRepository;
import ru.openai.gpt.repository.UserRepository;
import ru.openai.gpt.service.telegram.TelegramBotService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class BusinessService {
    private final MessageRepository messageRepository;
    private final UserService userService;
    private final MessageService messageService;
    private final MyOpenAiService openAiService;
    private final GptProps gptProps;
    private final TelegramBotService telegramBotService;


    @Transactional
    public void processMessage(
            Long telegramId,
            String userName,
            Integer telegramMessageId,
            String text
    ) {
        UserEntity user = userService.findOrCreateByTelegramIdAndName(telegramId, userName);
        MessageEntity message = messageService.create(text, telegramMessageId);
        user.addMessage(message);
        user = userService.save(user);


        String answer = "";
        try {
            answer = sendMessageAndReceiveAnswer(user, message.getRequest());
        } catch (Exception e) {
            answer = e.getMessage();
        }

        message.setResponse(answer);
        message = messageRepository.save(message);

        telegramBotService.editMarkup(user.getTelegramId(), message.getTelegramMessageId(), message.getResponse());
    }

    private String sendMessageAndReceiveAnswer(UserEntity user, String request) {

        AssistantV2 assistant = openAiService.retrieveAssistant(gptProps.getAssistantId());
        log.info("assistant: \n{}", assistant);

        if (Objects.nonNull(user.getThreadId())) {
            ThreadV2 thread = openAiService.retrieveThread(user.getThreadId());
            if (Objects.isNull(thread)) {
                user.setThreadId(null);
            }
        }

        if (Objects.isNull(user.getThreadId())) {
            ThreadV2 thread = openAiService.createThread(ThreadRequestV2.builder()
//                .toolResources(assistant.getToolResources())
                .build());
            log.info("thread: \n{}", thread);
            user.setThreadId(thread.getId());
            userService.save(user);
        }

        MessageRequestV2 messageRequest = MessageRequestV2.builder()
                .role("user")
                .content(request)
                .build();
        MessageV2 message = openAiService.createMessage(user.getThreadId(), messageRequest);
        log.info("message: \n{}", message);

        RunCreateRequestV2 runRequest = RunCreateRequestV2.builder()
                .assistantId(assistant.getId())
                .toolChoice(Map.of("type", "file_search"))
                .build();
        RunV2 run = openAiService.createRun(user.getThreadId(), runRequest);
        log.info("run: \n{}", run);

        while (!run.getStatus().equals("completed")) {
            try {
                Thread.sleep(1000l);
            } catch (Exception ignored) {}

            run = openAiService.retrieveRun(user.getThreadId(), run.getId());
            log.info("run: \n{}", run);
            if (List.of("failed", "cancelled", "expired").contains(run.getStatus())) {
                log.error("пришел ответ со статусом " + run.getStatus());
                throw new RuntimeException("пришел ответ со статусом " + run.getStatus());
            }
        }

        RunStepV2 runStep = openAiService.listRunSteps(user.getThreadId(), run.getId()).getData().get(0);
        log.info("runStep: \n{}", runStep);

        String messageId = runStep.getStepDetails().getMessageCreation().getMessageId();
        MessageV2 messageResponse = openAiService.retrieveMessage(user.getThreadId(), messageId);
        log.info("messageResponse: \n{}", messageResponse);
        return messageResponse.getContent().get(0).getText().getValue()
                .replaceAll("【\\d+:\\d+†source】", "");
    }
}
