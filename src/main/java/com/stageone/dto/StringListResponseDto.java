package com.stageone.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StringListResponseDto {


    private List<StringResponseDto> data;
    private Integer count;

    @JsonProperty("filters_applied")
    private StringFilterParamsDto filtersApplied;

}

