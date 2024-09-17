package ru.openai.gpt.model.files;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VectorStore {
    @JsonProperty("file_ids")
    List<String> fileIds;
    @JsonProperty("chunking_strategy")
    Object chunkingStrategy;
    Map<String, String> metadata;
}
