package ru.openai.gpt.model.runSteps;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.theokanning.openai.common.LastError;
import com.theokanning.openai.runs.StepDetails;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RunStepV2 {
    private String id;
    private String object;
    @JsonProperty("created_at")
    private Integer createdAt;
    @JsonProperty("assistant_id")
    private String assistantId;
    @JsonProperty("thread_id")
    private String threadId;
    @JsonProperty("run_id")
    private String runId;
    private String type;
    private String status;
    @JsonProperty("step_details")
    private StepDetails stepDetails;
    @JsonProperty("last_error")
    private LastError lastError;
    @JsonProperty("expired_at")
    private Integer expiredAt;
    @JsonProperty("cancelled_at")
    private Integer cancelledAt;
    @JsonProperty("failed_at")
    private Integer failedAt;
    @JsonProperty("completed_at")
    private Integer completedAt;
    private Map<String, String> metadata;
    private Object usage;
}
