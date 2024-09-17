package ru.openai.gpt.model.messages;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.theokanning.openai.assistants.AssistantFunction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.openai.gpt.model.files.FileSearch;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ToolV2 {
    AssistantToolsV2Enum type;
    @JsonProperty("file_search")
    FileSearch fileSearch;
    AssistantFunction function;
}
