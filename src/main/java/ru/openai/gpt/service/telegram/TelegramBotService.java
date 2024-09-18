package ru.openai.gpt.service.telegram;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.openai.gpt.entity.MessageEntity;
import ru.openai.gpt.entity.UserEntity;
import ru.openai.gpt.service.BusinessService;
import ru.openai.gpt.service.MessageService;
import ru.openai.gpt.service.UserService;

@Slf4j
@Setter
@Component
@EnableConfigurationProperties(TelegramProperties.class)
public class TelegramBotService extends TelegramLongPollingBot {

//    private ReplyKeyboardMarkup menu = TelegramMenu.init();
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
            return;
        } else if (update.hasMessage() && update.getMessage().hasText()) {
            String text = update.getMessage().getText();
            Integer telegramMessageId = sendMessage(chatId, "Команда обрабатывается");

            UserEntity user = userService.findOrCreateByTelegramIdAndName(chatId, update.getMessage().getForwardSenderName());
            MessageEntity message = messageService.create(user, text, telegramMessageId);
            businessService.sendMessageAndReceiveAnswer(user, message);

        }
    }



    public void editMarkup(Long chatId, Integer telegramMessageId, String text) {
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

    private Integer sendMessage(Long chatId, String textToSend) {
        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text(textToSend)
                .build();
        try {
            return execute(message).getMessageId();
        } catch (TelegramApiException e) {
            log.error("Can't send telegram message", e);
            throw new RuntimeException("Сообщение не отправилось в телеге");
        }
    }
}
