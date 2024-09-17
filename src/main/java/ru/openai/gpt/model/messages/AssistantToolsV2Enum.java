package ru.openai.gpt.model.messages;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum AssistantToolsV2Enum {
    @JsonProperty("code_interpreter")
    CODE_INTERPRETER,
    @JsonProperty("file_search")
    FILE_SEARCH;
}
