package com.stageone.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StringFilterParamsDto {
    @JsonProperty("is_palindrome")
    private Boolean isPalindrome;

    @JsonProperty("min_length")
    private Integer minLength;

    @JsonProperty("max_length")
    private Integer maxLength;

    @JsonProperty("word_count")
    private Integer wordCount;

    @JsonProperty("contains_character")
    private String containsCharacter;
}
