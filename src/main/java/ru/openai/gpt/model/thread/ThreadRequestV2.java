package ru.openai.gpt.model.thread;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.openai.gpt.model.files.ToolResources;
import ru.openai.gpt.model.messages.MessageRequestV2;

import java.util.List;
import java.util.Map;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ThreadRequestV2 {
    List<MessageRequestV2> messages;

    @JsonProperty("tool_resources")
    ToolResources toolResources;
    Map<String, String> metadata;
}
