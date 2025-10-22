package com.stageone.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StringRequestDto {
    @NotNull(message = "Value field is required")
    private String value;

    public String getValue() {
        return value;
    }
}
