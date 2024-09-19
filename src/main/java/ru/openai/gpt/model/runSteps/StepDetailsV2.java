package ru.openai.gpt.model.runSteps;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.theokanning.openai.runs.MessageCreation;
import com.theokanning.openai.runs.ToolCall;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.openai.gpt.model.messages.ToolV2;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StepDetailsV2 {
    private String type;
    @JsonProperty("message_creation")
    private MessageCreation messageCreation;
    @JsonProperty("tool_calls")
    private List<ToolV2> toolCalls;
}
