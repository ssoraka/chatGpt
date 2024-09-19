package ru.openai.gpt.model.runs;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.theokanning.openai.common.LastError;
import com.theokanning.openai.runs.RequiredAction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.openai.gpt.model.messages.ToolV2;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RunV2 {
    private String id;
    private String object;
    @JsonProperty("created_at")
    private Integer createdAt;
    @JsonProperty("thread_id")
    private String threadId;
    @JsonProperty("assistant_id")
    private String assistantId;
    private String status;
    @JsonProperty("required_action")
    private RequiredAction requiredAction;
    @JsonProperty("last_error")
    private LastError lastError;
    @JsonProperty("expires_at")
    private Integer expiresAt;
    @JsonProperty("started_at")
    private Integer startedAt;
    @JsonProperty("cancelled_at")
    private Integer cancelledAt;
    @JsonProperty("failed_at")
    private Integer failedAt;
    @JsonProperty("completed_at")
    private Integer completedAt;
    @JsonProperty("incomplete_details")
    private Object incompleteDetails;
    private String model;
    private String instructions;
    private List<ToolV2> tools;
    private Map<String, String> metadata;
    private Double temperature;
    @JsonProperty("top_p")
    private Double topP;
    private Boolean stream;
    @JsonProperty("max_prompt_tokens")
    private Integer maxPromptTokens;
    @JsonProperty("max_completion_tokens")
    private Integer maxCompletionTokens;
    @JsonProperty("truncation_strategy")
    private Object truncationStrategy;
    @JsonProperty("tool_choice")
    private Object toolChoice;
    @JsonProperty("parallel_tool_calls")
    private Boolean parallelToolCalls;
    @JsonProperty("response_format")
    private Object responseFormat;
}
