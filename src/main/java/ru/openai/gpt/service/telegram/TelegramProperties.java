package ru.openai.gpt.service.telegram;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties("telegram")
public class TelegramProperties {
    private String bot;
    private String token;
    private Boolean enable;
}
