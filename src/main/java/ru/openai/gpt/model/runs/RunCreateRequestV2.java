package ru.openai.gpt.model.runs;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.theokanning.openai.assistants.Tool;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.openai.gpt.model.messages.MessageV2;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RunCreateRequestV2 {
    String assistantId;
    String model;
    String instructions;
    @JsonProperty("additional_instructions")
    String additionalInstructions;
    @JsonProperty("additional_messages")
    List<MessageV2> additionalMessages;
    List<Tool> tools;
    Map<String, String> metadata;
    Double temperature;
    @JsonProperty("top_p")
    Double topP;
    Boolean stream;
    @JsonProperty("max_prompt_tokens")
    Integer maxPromptTokens;
    @JsonProperty("max_completion_tokens")
    Integer maxCompletionTokens;
    @JsonProperty("truncation_strategy")
    Object truncationStrategy;
    @JsonProperty("tool_choice")
    String toolChoice;
    @JsonProperty("parallel_tool_calls")
    Boolean parallelToolCalls;
    @JsonProperty("response_format")
    Object responseFormat;
}
