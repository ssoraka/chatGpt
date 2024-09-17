package ru.openai.gpt.model.assistant;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import ru.openai.gpt.model.files.ToolResources;
import ru.openai.gpt.model.messages.ToolV2;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssistantV2 {
    String id;
    String object;
    @JsonProperty("created_at")
    Integer createdAt;
    String name;
    String description;
    @NonNull String model;
    String instructions;
    List<ToolV2> tools;
    @JsonProperty("tool_resources")
    ToolResources toolResources;
    Map<String, String> metadata;
    Double temperature;
    @JsonProperty("top_p")
    Double topP;
    @JsonProperty("response_format")
    Object responseFormat;
}
