package ru.openai.gpt.model.messages;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Attachment {
    @JsonProperty("file_id")
    String fileId;
    List<ToolV2> tools;
}
