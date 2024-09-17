package ru.openai.gpt.model.messages;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.theokanning.openai.messages.MessageContent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageV2 {
    String id;
    String object;
    @JsonProperty("created_at")
    int createdAt;
    @JsonProperty("thread_id")
    String threadId;
    String role;
    List<MessageContent> content;
    @JsonProperty("assistant_id")
    String assistantId;
    @JsonProperty("run_id")
    String runId;
    @JsonProperty("attachments")
    List<Attachment> attachments;
    Map<String, String> metadata;
}
