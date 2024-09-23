package ru.openai.gpt.model.messages;

import lombok.*;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageRequestV2 {
    @NonNull String role;
    @NonNull String content;
    List<Attachment> attachments;
}
