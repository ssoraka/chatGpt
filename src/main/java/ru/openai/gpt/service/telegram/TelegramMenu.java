package ru.openai.gpt.service.telegram;

import org.apache.commons.lang3.tuple.Pair;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.openai.gpt.constant.ApplicationConstants;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class TelegramMenu {
    public static final String START_CMD = "/start";
    public static final String PROFILE_CMD = "/profile";
    public static final String USERS_CMD = "/users";
    public static final String CALENDARS_CMD = "/calendar";
    public static final String RECORDS_CMD = "/records";
    public static final String DELETE_RECORD_CMD = "delete/record";
    public static final String RECORD_CMD = "/record";
    public static final String DATE_CMD = "/date";
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
        keyboardRows.add(new KeyboardRow(List.of(new KeyboardButton(RECORDS_CMD))));
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


    public static InlineKeyboardMarkup calendar() {
        return calendar(LocalDate.now());
    }
    public static InlineKeyboardMarkup calendar(LocalDate date) {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        LocalDate startOfMonth = LocalDate.of(date.getYear(), date.getMonth().getValue(), 1);


        header(buttons, startOfMonth);

        List<InlineKeyboardButton> button = new ArrayList<>();
        buttons.add(button);

        for (int i = 1; i < startOfMonth.getDayOfWeek().getValue(); i++) {
            button.add(InlineKeyboardButton.builder()
                    .text(" ")
                    .callbackData("empty")
                    .build());
        }

        for (int i = 0; i < startOfMonth.getMonth().maxLength(); i++) {
            if (button.size() == 7) {
                button = new ArrayList<>();
                buttons.add(button);
            }

            LocalDate day = startOfMonth.plusDays(i);

            button.add(InlineKeyboardButton.builder()
                    .text("" + (i + 1))
                    .callbackData(startOfMonth.plusDays(i).format(ApplicationConstants.DATE_FORMATTER))
                    .build());
        }

        while (button.size() < 7) {
            button.add(InlineKeyboardButton.builder()
                    .text(" ")
                    .callbackData("empty")
                    .build());
        }

        button = new ArrayList<>();
        buttons.add(button);
        button.add(InlineKeyboardButton.builder()
                .text("назад")
                .callbackData(START_CMD)
                .build());

        InlineKeyboardMarkup markupKeyboard = new InlineKeyboardMarkup();
        markupKeyboard.setKeyboard(buttons);
        return markupKeyboard;
    }

    public static InlineKeyboardMarkup dayTable(LocalDate date, List<LocalDateTime> busy) {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        LocalDateTime time = LocalDateTime.of(date, LocalTime.of(9, 0, 0));

        for (int i = 0; i < 10; i++) {
            if (busy.contains(time.plusHours(i))) {
                continue;
            }

            List<InlineKeyboardButton> button = new ArrayList<>();
            buttons.add(button);
            button.add(InlineKeyboardButton.builder()
                    .text(time.plusHours(i).format(ApplicationConstants.DATE_TIME_FORMATTER))
                    .callbackData(DATE_CMD + "/" + time.plusHours(i).format(ApplicationConstants.DATE_TIME_FORMATTER))
                    .build());
        }

        List<InlineKeyboardButton> button = new ArrayList<>();
        buttons.add(button);
        button.add(InlineKeyboardButton.builder()
                .text("назад")
                .callbackData(DATE_CMD + "/" + date.format(ApplicationConstants.DATE_FORMATTER))
                .build());

        InlineKeyboardMarkup markupKeyboard = new InlineKeyboardMarkup();
        markupKeyboard.setKeyboard(buttons);
        return markupKeyboard;
    }


    private static void header(List<List<InlineKeyboardButton>> buttons, LocalDate startOfMonth) {
        List<InlineKeyboardButton> button = new ArrayList<>();
        buttons.add(button);
        button.add(InlineKeyboardButton.builder()
                .text("<")
                .callbackData(DATE_CMD + "/" + startOfMonth.minusMonths(1).format(ApplicationConstants.DATE_FORMATTER))
                .build());
        button.add(InlineKeyboardButton.builder()
                .text("месяц " + startOfMonth.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault()))
                .callbackData("1")
                .build());
        button.add(InlineKeyboardButton.builder()
                .text(">")
                .callbackData(DATE_CMD + "/" + startOfMonth.plusMonths(1).format(ApplicationConstants.DATE_FORMATTER))
                .build());

        button = new ArrayList<>();
        buttons.add(button);
        for (String s : new String[]{"П", "В", "С", "Ч", "П", "С", "В"}) {
            button.add(InlineKeyboardButton.builder()
                    .text(s)
                    .callbackData("1")
                    .build());
        }
    }

    public static InlineKeyboardMarkup currentRecord(LocalDateTime dateTime) {
        List<List<InlineKeyboardButton>> buttons = List.of(
                List.of(InlineKeyboardButton.builder()
                        .text("удалить запись на " + dateTime.format(ApplicationConstants.DATE_TIME_FORMATTER))
                        .callbackData(DELETE_RECORD_CMD+ "/" + dateTime.format(ApplicationConstants.DATE_TIME_FORMATTER))
                        .build()),
                List.of(InlineKeyboardButton.builder()
                        .text("назад")
                        .callbackData(RECORDS_CMD)
                        .build())
        );

        InlineKeyboardMarkup markupKeyboard = new InlineKeyboardMarkup();
        markupKeyboard.setKeyboard(buttons);
        return markupKeyboard;
    }

    public static InlineKeyboardMarkup getRecords(List<Pair<LocalDateTime, String>> records) {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();

        for (Pair<LocalDateTime, String> pair : records) {
            List<InlineKeyboardButton> button = new ArrayList<>();
            buttons.add(button);
            button.add(InlineKeyboardButton.builder()
                    .text(pair.getKey().format(ApplicationConstants.DATE_TIME_FORMATTER) + " " + pair.getValue())
                    .callbackData(RECORD_CMD + "/" + pair.getKey().format(ApplicationConstants.DATE_TIME_FORMATTER))
                    .build());
        }

        List<InlineKeyboardButton> button = new ArrayList<>();
        buttons.add(button);
        button.add(InlineKeyboardButton.builder()
                .text("назад")
                .callbackData(START_CMD)
                .build());

        InlineKeyboardMarkup markupKeyboard = new InlineKeyboardMarkup();
        markupKeyboard.setKeyboard(buttons);
        return markupKeyboard;
    }
}
