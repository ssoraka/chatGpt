package ru.openai.gpt.service.telegram;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.openai.gpt.service.BusinessService;

@Configuration
@RequiredArgsConstructor
public class TelegramConfig {

    private final TelegramBotService telegramBotService;
    private final BusinessService businessService;;


    @PostConstruct
    void initMessageService() throws TelegramApiException {
        telegramBotService.setBusinessService(businessService);
        telegramBotService.init();
    }
}
