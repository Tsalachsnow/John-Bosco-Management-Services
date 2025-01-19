package com.johnboscoltd.services;

import com.johnboscoltd.dto.ApproveOrRejectLoanRequestDto;
import com.johnboscoltd.dto.GenericResponseDto;
import com.johnboscoltd.dto.GetLoanDetailsResponseDto;
import com.johnboscoltd.dto.LoanRequestDto;

public interface LoanService {
    LoanRequestDto.Response loanRequest(LoanRequestDto.Request request);
    GetLoanDetailsResponseDto getLoanDetails(String LoanReferenceId);
    GenericResponseDto approveLoan(ApproveOrRejectLoanRequestDto request);
    GenericResponseDto rejectLoan(ApproveOrRejectLoanRequestDto request);
}
