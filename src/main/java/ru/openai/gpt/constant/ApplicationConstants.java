package ru.openai.gpt.constant;

import java.time.format.DateTimeFormatter;

public class ApplicationConstants {
    public static DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    public static DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
}
