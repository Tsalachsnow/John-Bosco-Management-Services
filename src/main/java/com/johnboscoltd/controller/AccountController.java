package com.johnboscoltd.controller;

import com.johnboscoltd.dto.CreateAccountDto;
import com.johnboscoltd.dto.FundAccountDto;
import com.johnboscoltd.dto.GetAccountStatementDto;
import com.johnboscoltd.services.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/account")
public class AccountController {

    private final AccountService accountService;

    @Operation(summary = "Create Customer Account Endpoint")
    @PostMapping(path = "/create",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public CreateAccountDto.Response createAccount(
            @RequestBody CreateAccountDto.Request request) {
        return accountService.createAccount(request);
    }

    @Operation(summary = "Fund Customer Account Endpoint")
    @PostMapping(path = "/fund",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public FundAccountDto.Response fundAccount(
            @RequestBody FundAccountDto.Request request) {
        return accountService.fundAccount(request);
    }

    @Operation(summary = "Get Transaction Statement Endpoint")
    @PostMapping(path = "/transactions/history",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<GetAccountStatementDto.Response> getTransactionHistory(
            @RequestBody GetAccountStatementDto.Request request) {
        return accountService.getAccountStatement(request);
    }
}
