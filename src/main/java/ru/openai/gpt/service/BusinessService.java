package ru.openai.gpt.service;

import com.theokanning.openai.file.File;
import com.theokanning.openai.service.OpenAiService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.openai.gpt.config.GptProps;
import ru.openai.gpt.entity.MessageEntity;
import ru.openai.gpt.entity.ThreadEntity;
import ru.openai.gpt.entity.UserEntity;
import ru.openai.gpt.enums.Action;
import ru.openai.gpt.model.assistant.AssistantV2;
import ru.openai.gpt.model.messages.*;
import ru.openai.gpt.model.runSteps.RunStepV2;
import ru.openai.gpt.model.runs.RunCreateRequestV2;
import ru.openai.gpt.model.runs.RunV2;
import ru.openai.gpt.model.thread.ThreadRequestV2;
import ru.openai.gpt.model.thread.ThreadV2;
import ru.openai.gpt.repository.MessageRepository;
import ru.openai.gpt.service.telegram.TelegramBotService;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class BusinessService {
    private final MessageRepository messageRepository;
    private final UserService userService;
    private final ThreadService threadService;
    private final MessageService messageService;
    private final MyOpenAiService myOpenAiService;
    private final OpenAiService openAiService;
    private final GptProps gptProps;
    private final TelegramBotService telegramBotService;


    @Transactional
    public String processMessage(
            UUID userId,
            Integer telegramMessageId,
            String text
    ) {
        UserEntity user = userService.findByIdOrThrowNotFound(userId);
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
        return message.getResponse();
    }


    @Transactional
    @SneakyThrows
    public void loadProfile(UUID userId, String text) {
        File file = myOpenAiService.uploadFile("assistants", text);

        UserEntity user = userService.findByIdOrThrowNotFound(userId);
        if (Objects.nonNull(user.getFileId())) {
            openAiService.deleteFile(user.getFileId());
            //надо во все треды пользователя добавить этот файл, а старый удалить.
        }

        user.setAction(Action.NONE);
        user.setProfile(text);
        user.setFileId(file.getId());
        userService.save(user);


    }

    @Transactional
    public void updateStatus(Long telegramId, Action action) {
        UserEntity user = userService.findByTelegramIdOrThrowNotFound(telegramId);
        user.setAction(action);
        userService.save(user);
    }

    @Transactional
    public void startThread(Long telegramId, UUID receiverId) {
        UserEntity sender = userService.findByTelegramIdOrThrowNotFound(telegramId);
        UserEntity receiver = userService.findByIdOrThrowNotFound(receiverId);

        if (Objects.nonNull(sender.getCurrentThread()) && sender.getCurrentThread().getReceiver().getId().equals(receiverId)) {
            return;
        }

        ThreadEntity thread = ThreadEntity.builder()
                .sender(sender)
                .receiver(receiver)
                .build();
        sender.setCurrentThread(thread);
        threadService.save(thread);
        userService.save(sender);
    }

    private String sendMessageAndReceiveAnswer(UserEntity user, String request) {

        AssistantV2 assistant = myOpenAiService.retrieveAssistant(gptProps.getAssistantId());
        log.info("assistant: \n{}", assistant);

        ThreadEntity threadEntity = user.getCurrentThread();
        if (Objects.nonNull(threadEntity.getThreadId())) {
            ThreadV2 thread = myOpenAiService.retrieveThread(threadEntity.getThreadId());
            if (Objects.isNull(thread)) {
                threadEntity.setThreadId(null);
            }
        }

        if (Objects.isNull(threadEntity.getThreadId())) {
            ThreadV2 thread = myOpenAiService.createThread(ThreadRequestV2.builder()
                .build());
            log.info("thread: \n{}", thread);
            threadEntity.setThreadId(thread.getId());
            threadService.save(threadEntity);
        }
        String threadId = threadEntity.getThreadId();

        MessageRequestV2 messageRequest = MessageRequestV2.builder()
                .role("user")
                .content(request)
                .attachments(List.of(Attachment.builder()
                                .fileId(threadEntity.getReceiver().getFileId())
                                .tools(List.of(ToolV2.builder()
                                        .type(AssistantToolsV2Enum.FILE_SEARCH)
                                        .build()))
                        .build()))
                .build();
        MessageV2 message = myOpenAiService.createMessage(threadId, messageRequest);
        log.info("message: \n{}", message);

        RunCreateRequestV2 runRequest = RunCreateRequestV2.builder()
                .assistantId(assistant.getId())
                .toolChoice(Map.of("type", "file_search"))
                .build();
        RunV2 run = myOpenAiService.createRun(threadId, runRequest);
        log.info("run: \n{}", run);

        while (!run.getStatus().equals("completed")) {
            try {
                Thread.sleep(1000l);
            } catch (Exception ignored) {}

            run = myOpenAiService.retrieveRun(threadId, run.getId());
            log.info("run: \n{}", run);
            if (List.of("failed", "cancelled", "expired").contains(run.getStatus())) {
                log.error("пришел ответ со статусом " + run.getStatus());
                throw new RuntimeException("пришел ответ со статусом " + run.getStatus());
            }
        }

        RunStepV2 runStep = myOpenAiService.listRunSteps(threadId, run.getId()).getData().get(0);
        log.info("runStep: \n{}", runStep);

        String messageId = runStep.getStepDetails().getMessageCreation().getMessageId();
        MessageV2 messageResponse = myOpenAiService.retrieveMessage(threadId, messageId);
        log.info("messageResponse: \n{}", messageResponse);
        return messageResponse.getContent().get(0).getText().getValue()
                .replaceAll("【\\d+:\\d+†source】", "");
    }


    public String sendCvs(java.io.File file) {
        File fileGpt = openAiService.uploadFile("assistants", file.getPath().toString());

        AssistantV2 assistant = myOpenAiService.retrieveAssistant(gptProps.getAssistantCvsId());
        log.info("assistant: \n{}", assistant);

        String threadId = "thread_8gsSEqh425ZRpCaprEbOOz6d";
        ThreadV2 thread = myOpenAiService.retrieveThread(threadId);

        MessageRequestV2 messageRequest = MessageRequestV2.builder()
                .content("Напиши результат в нескольких предложениях.")
                .role("user")
                .attachments(List.of(Attachment.builder()
                        .fileId(fileGpt.getId())
                        .tools(List.of(ToolV2.builder()
                                .type(AssistantToolsV2Enum.FILE_SEARCH)
                                .build()))
                        .build()))
                .build();

        MessageV2 message = myOpenAiService.createMessage(threadId, messageRequest);
        log.info("message: \n{}", message);

        RunCreateRequestV2 runRequest = RunCreateRequestV2.builder()
                .assistantId(assistant.getId())
                .toolChoice(Map.of("type", "file_search"))
                .build();
        RunV2 run = myOpenAiService.createRun(threadId, runRequest);
        log.info("run: \n{}", run);

        while (!run.getStatus().equals("completed")) {
            try {
                Thread.sleep(2000l);
            } catch (Exception ignored) {}

            run = myOpenAiService.retrieveRun(threadId, run.getId());
            log.info("run: \n{}", run);
            if (List.of("failed", "cancelled", "expired").contains(run.getStatus())) {
                log.error("пришел ответ со статусом " + run.getStatus());
                throw new RuntimeException("пришел ответ со статусом " + run.getStatus());
            }
        }

        openAiService.deleteFile(fileGpt.getId());


        RunStepV2 runStep = myOpenAiService.listRunSteps(threadId, run.getId()).getData().get(0);
        log.info("runStep: \n{}", runStep);

        String messageId = runStep.getStepDetails().getMessageCreation().getMessageId();
        MessageV2 messageResponse = myOpenAiService.retrieveMessage(threadId, messageId);
        log.info("messageResponse: \n{}", messageResponse);
        return messageResponse.getContent().get(0).getText().getValue()
                .replaceAll("【\\d+:\\d+†source】", "");
    }
}
