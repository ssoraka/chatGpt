package ru.openai.gpt.model.messages;

import lombok.*;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageRequestV2 {
    @NonNull String role;
    @NonNull String content;
}
