package com.johnboscoltd.controller;

import com.johnboscoltd.dto.ApproveOrRejectLoanRequestDto;
import com.johnboscoltd.dto.GenericResponseDto;
import com.johnboscoltd.dto.GetLoanDetailsResponseDto;
import com.johnboscoltd.dto.LoanRequestDto;
import com.johnboscoltd.services.LoanService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/loan")
public class LoanController {
    private final LoanService loanService;


    @Operation(summary = "Loan Request Endpoint")
    @PostMapping(path = "/create",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public LoanRequestDto.Response requestLoan(
            @RequestBody LoanRequestDto.Request request) {
        return loanService.loanRequest(request);
    }

    @Operation(summary = "Get Loan Details Endpoint")
    @GetMapping(path = "/enquiry/{loanReferenceId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public GetLoanDetailsResponseDto requestLoan(@PathVariable String loanReferenceId) {
        return loanService.getLoanDetails(loanReferenceId);
    }

    @Operation(summary = "Approve Loan Endpoint")
    @PostMapping(path = "/approve",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public GenericResponseDto approveLoan(
            @RequestBody ApproveOrRejectLoanRequestDto request) {
        return loanService.approveLoan(request);
    }


    @Operation(summary = "Reject Loan Endpoint")
    @PostMapping(path = "/reject",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public GenericResponseDto rejectLoan(
            @RequestBody ApproveOrRejectLoanRequestDto request) {
        return loanService.rejectLoan(request);
    }
}
