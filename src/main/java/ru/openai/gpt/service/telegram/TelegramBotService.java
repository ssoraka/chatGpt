package ru.openai.gpt.service.telegram;

import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.openai.gpt.entity.UserEntity;
import ru.openai.gpt.enums.Action;
import ru.openai.gpt.service.BusinessService;
import ru.openai.gpt.service.MessageService;
import ru.openai.gpt.service.UserService;
import ru.openai.gpt.utils.MessageUtils;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Setter
@Component
@EnableConfigurationProperties(TelegramProperties.class)
public class TelegramBotService extends TelegramLongPollingBot {

    private ReplyKeyboardMarkup menu = TelegramMenu.init();
    private InlineKeyboardMarkup upload = TelegramMenu.load();
//    private ReplyKeyboardMarkup adminMenu = TelegramMenu.admin();
    private TelegramProperties properties;
    private UserService userService;
    private MessageService messageService;
    private BusinessService businessService;

    public TelegramBotService(TelegramProperties properties,
                              UserService userService,
                              MessageService messageService
                              ) {
        super(properties.getToken());
        this.properties = properties;
        this.userService = userService;
        this.messageService = messageService;
    }

    public void init() throws TelegramApiException {
        if (BooleanUtils.isFalse(properties.getEnable())) {
            return;
        }
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(this);
    }

    @Override
    public String getBotUsername() {
        return properties.getBot();
    }

    @Override
    @Transactional
    public void onUpdateReceived(Update update) {
        long chatId;
        if (update.hasCallbackQuery()) {
            chatId = update.getCallbackQuery().getMessage().getChatId();
        } else if (update.hasMessage()) {
            chatId = update.getMessage().getChatId();
        } else {
            return;
        }


//
        if (update.hasCallbackQuery()) {
            //работа с кнопками под сообщениями
            String data = update.getCallbackQuery().getData();
            Integer messageId = update.getCallbackQuery().getMessage().getMessageId();
            if (data.equals(TelegramMenu.UPLOAD_CMD)) {
                businessService.updateStatus(chatId, Action.UPLOAD);
                delete(chatId, messageId);
                sendMessage(chatId, "Расскажите о себе подроблее...");
            } else if (data.startsWith(TelegramMenu.USER_CMD) && MessageUtils.isUUID(data.replace(TelegramMenu.USER_CMD, ""))) {
                UUID receiverId = UUID.fromString(data.replace(TelegramMenu.USER_CMD, ""));
                businessService.startThread(chatId, receiverId);
                delete(chatId, messageId);
                sendMessage(chatId, "Что вас интересует?");
            }
            return;
        } else if (update.hasMessage() && update.getMessage().hasDocument()) {
            java.io.File file = readFile(chatId, update.getMessage().getDocument());
            String answer = businessService.sendCvs(file);
            boolean delete = file.delete();
            sendMessage(chatId, answer);
            return;
        } else if (update.hasMessage() && update.getMessage().hasText()) {
            String text = update.getMessage().getText();
            String userName = update.getMessage().getFrom().getUserName();

            UserEntity user = userService.findOrCreateByTelegramIdAndName(chatId, userName);

            switch (text) {
                case TelegramMenu.START_CMD : sendMessage(chatId, "Добрый день. Что вас интересует?"); return;
                case TelegramMenu.PROFILE_CMD : sendMessage(chatId, Objects.requireNonNullElse(user.getProfile(), "Вы не загрузили профиль"), upload); return;
                case TelegramMenu.USERS_CMD : sendMessage(chatId, "Пользователи", getUserNames()); return;
                default: {

                    if (Action.UPLOAD.equals(user.getAction())) {
                        businessService.loadProfile(user.getId(), text);
                        sendMessage(chatId, "Ваш профиль обновлен");
                    } else {
                        Integer telegramMessageId = sendMessage(chatId, "Команда обрабатывается");
                        String answer = businessService.processMessage(user.getId(), telegramMessageId, text);
                        delete(user.getTelegramId(), telegramMessageId);
                        sendMessage(user.getTelegramId(), answer);
                    }
                }
            }
        }
    }

    @SneakyThrows
    private java.io.File readFile(long chatId, Document document) {
        GetFile getFile = new GetFile();
        getFile.setFileId(document.getFileId());
        File execute = execute(getFile);

        java.io.File file1 = new java.io.File(document.getFileId() + "_" + document.getFileName());
        return downloadFile(execute, file1);
    }


    private ReplyKeyboard getUserNames() {
        List<Pair<String, String>> userNames = userService.findAllWithProfile().stream()
                .map(u -> Pair.of(u.getName(), TelegramMenu.USER_CMD + u.getId().toString()))
                .collect(Collectors.toList());
        return TelegramMenu.inline(userNames);
    }


    public void editMessage(Long chatId, Integer telegramMessageId, String text) {
        EditMessageText edit = EditMessageText.builder()
                .chatId(chatId)
                .messageId(telegramMessageId)
                .text(text)
                .build();

        try {
            execute(edit);
        } catch (TelegramApiException e) {
            log.error("Can't send telegram message", e);
        }
    }

    public void editMessageReplyMarkup(Long chatId, Integer telegramMessageId, InlineKeyboardMarkup markup) {
        EditMessageReplyMarkup edit = EditMessageReplyMarkup.builder()
                .chatId(chatId)
                .messageId(telegramMessageId)
                .replyMarkup(markup)
                .build();

        try {
            execute(edit);
        } catch (TelegramApiException e) {
            log.error("Can't send telegram message", e);
        }
    }

    public void delete(Long chatId, Integer telegramMessageId) {
        DeleteMessage delete = DeleteMessage.builder()
                .chatId(chatId)
                .messageId(telegramMessageId)
                .build();

        try {
            execute(delete);
        } catch (TelegramApiException e) {
            log.error("Can't send telegram message", e);
        }
    }


//    public void sendGachiStiker(Long chatId) {
//        SendSticker message = SendSticker.builder()
//                .chatId(chatId)
//                .sticker(new InputFile(GachiUtils.randomGachiStikerFileId()))
//                .build();
//        try {
//            execute(message);
//            log.info("Отправил стикер");
//        } catch (TelegramApiException ex) {
//            log.error("Ошибка при отправке стикер\n{}", ex.getMessage());
//            ex.printStackTrace();
//        }
//    }

    public Integer sendMessage(Long chatId, String textToSend) {
        return sendMessage(chatId, textToSend, menu);
    }

    private Integer sendMessage(Long chatId, String textToSend, ReplyKeyboard markup) {
        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text(textToSend)
                .build();
        if (Objects.nonNull(markup)) {
            message.setReplyMarkup(markup);
        }
        try {
            return execute(message).getMessageId();
        } catch (TelegramApiException e) {
            log.error("Can't send telegram message", e);
            throw new RuntimeException("Сообщение не отправилось в телеге");
        }
    }
}
