package com.johnboscoltd.services;

import com.johnboscoltd.dto.CreateAccountDto;
import com.johnboscoltd.dto.FundAccountDto;
import com.johnboscoltd.dto.GenericResponseDto;
import com.johnboscoltd.dto.GetAccountStatementDto;
import com.johnboscoltd.model.TransactionHistory;
import org.springframework.data.domain.Page;

public interface AccountService {
    CreateAccountDto.Response createAccount(CreateAccountDto.Request request);
    FundAccountDto.Response fundAccount(FundAccountDto.Request request);
    Page<GetAccountStatementDto.Response> getAccountStatement(GetAccountStatementDto.Request request);
}
