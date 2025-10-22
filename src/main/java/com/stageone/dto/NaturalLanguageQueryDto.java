package com.stageone.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NaturalLanguageQueryDto {
    private String originalQuery;
    private StringFilterParamsDto filters;
}
