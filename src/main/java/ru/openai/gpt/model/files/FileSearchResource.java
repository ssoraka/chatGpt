package ru.openai.gpt.model.files;

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
public class FileSearchResource {
    @JsonProperty("vector_store_ids")
    List<String> vectorStoreIds;
    @JsonProperty("vector_stores")
    List<VectorStore> vectorStores;
}
