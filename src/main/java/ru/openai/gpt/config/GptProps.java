package ru.openai.gpt.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties("openai.chatgpt")
public class GptProps {
    private String token;
    private String assistantId;
    private String assistantCvsId;
}
