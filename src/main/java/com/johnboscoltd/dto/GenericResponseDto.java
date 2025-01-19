package com.johnboscoltd.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class GenericResponseDto {
    private String responseCode;
    private String responseMessage;
}
