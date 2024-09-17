package ru.openai.gpt.model.files;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileSearch {
    @JsonProperty("max_num_results")
    int maxNumResults;
    @JsonProperty("ranking_options")
    Object rankingOptions;
}
