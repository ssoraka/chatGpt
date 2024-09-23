package ru.openai.gpt.service.telegram;

import org.apache.commons.lang3.tuple.Pair;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TelegramMenu {

    public static final String START_CMD = "/start";
    public static final String PROFILE_CMD = "profile";
    public static final String USERS_CMD = "users";
    public static final String CONTACT_CMD = "поделиться контактом";
    public static final String UPLOAD_CMD = "update";
    public static final String USER_CMD = "/user/";
    public static final String NOTIFICATION_CMD = "/notification/";
    public static final String REGISTER_USER_CMD = "/registerUserCount";
    public static final String ACTIVATE_QR_CMD = "/activateQrCount";
    public static final String SEND_NOTIFICATION_CMD = "/sendNotificationCount";
    public static final String READ_NOTIFICATION_CMD = "/readNotificationCount";

    public static ReplyKeyboardMarkup init() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true); //подгоняем размер
        replyKeyboardMarkup.setOneTimeKeyboard(true); //скрываем после использования

        ArrayList<KeyboardRow> keyboardRows = new ArrayList<>();
        replyKeyboardMarkup.setKeyboard(keyboardRows);

        keyboardRows.add(new KeyboardRow(List.of(new KeyboardButton(PROFILE_CMD))));
        keyboardRows.add(new KeyboardRow(List.of(new KeyboardButton(USERS_CMD))));
        return replyKeyboardMarkup;
    }


    public static InlineKeyboardMarkup load() {
        List<List<InlineKeyboardButton>> buttons = List.of(List.of(InlineKeyboardButton.builder()
                        .text("Загрузить новый файл")
                        .callbackData(UPLOAD_CMD)
                        .build()));

        InlineKeyboardMarkup markupKeyboard = new InlineKeyboardMarkup();
        markupKeyboard.setKeyboard(buttons);
        return markupKeyboard;
    }

    public static ReplyKeyboardMarkup contact() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true); //подгоняем размер
        replyKeyboardMarkup.setOneTimeKeyboard(false); //скрываем после использования

        ArrayList<KeyboardRow> keyboardRows = new ArrayList<>();
        replyKeyboardMarkup.setKeyboard(keyboardRows);

        KeyboardButton button = new KeyboardButton(CONTACT_CMD);
        button.setRequestContact(true);

        keyboardRows.add(new KeyboardRow(List.of(button)));
        return replyKeyboardMarkup;
    }

    public static InlineKeyboardMarkup inline(List<Pair<String, String>> names) {
        List<List<InlineKeyboardButton>> buttons = names.stream()
                .map(name -> List.of(InlineKeyboardButton.builder()
                        .text(name.getKey())
                        .callbackData(name.getValue())
                        .build()))
                .collect(Collectors.toList());

        InlineKeyboardMarkup markupKeyboard = new InlineKeyboardMarkup();
        markupKeyboard.setKeyboard(buttons);
        return markupKeyboard;
    }
}
