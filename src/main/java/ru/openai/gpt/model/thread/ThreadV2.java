package ru.openai.gpt.model.thread;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.openai.gpt.model.files.ToolResources;

import java.util.Map;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ThreadV2 {
    String id;
    String object;
    @JsonProperty("created_at")
    int createdAt;
    @JsonProperty("tool_resources")
    ToolResources toolResources;
    Map<String, String> metadata;
}
