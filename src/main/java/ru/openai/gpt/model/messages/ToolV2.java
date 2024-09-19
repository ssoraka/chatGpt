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
    String id;
    AssistantToolsV2Enum type;
    @JsonProperty("code_interpreter")
    Object codeInterpreter;
    @JsonProperty("file_search")
    FileSearch fileSearch;
    AssistantFunction function;
}
