package com.johnboscoltd.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ApproveOrRejectLoanRequestDto {
    private String loanReferenceId;
}
