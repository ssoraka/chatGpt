package ru.openai.gpt.model.files;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ToolResources {
    @JsonProperty("file_search")
    FileSearchResource fileSearch;

    @JsonProperty("code_interpreter")
    CodeInterpreter codeInterpreter;

}
