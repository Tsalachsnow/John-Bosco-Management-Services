package com.johnboscoltd.services.impl;

import com.johnboscoltd.config.JacksonConfig;
import com.johnboscoltd.dto.ApproveOrRejectLoanRequestDto;
import com.johnboscoltd.dto.GenericResponseDto;
import com.johnboscoltd.dto.GetLoanDetailsResponseDto;
import com.johnboscoltd.dto.LoanRequestDto;
import com.johnboscoltd.enums.Product;
import com.johnboscoltd.constants.ResponseCode;
import com.johnboscoltd.constants.ResponseMessage;
import com.johnboscoltd.exceptions.GenericException;
import com.johnboscoltd.model.Account;
import com.johnboscoltd.model.Loan;
import com.johnboscoltd.services.LoanService;
import com.johnboscoltd.services.repositories.AccountRepository;
import com.johnboscoltd.services.repositories.LoanRepository;
import com.johnboscoltd.util.JsonConverter;
import com.johnboscoltd.util.ReferenceNumberGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoanServiceImpl implements LoanService {

    private final LoanRepository repository;
    private final AccountRepository accountRepository;
    private final JacksonConfig objectMapper;

    @Override
    public LoanRequestDto.Response loanRequest(LoanRequestDto.Request request){
        LoanRequestDto.Response response = new LoanRequestDto.Response();
        Optional<Account> accountOptional =  accountRepository.findById(request.getAccountNo());
        Loan loan = new Loan();
        if(accountOptional.isPresent()){
           Account account = accountOptional.get();
            Optional<Loan> loanOptional =
                    repository.findByCustomerIdAndIsApprovedAndIsLiquidated(
                            account.getCustomer().getId(), true, false);
            if(loanOptional.isPresent()){
                throw new GenericException(ResponseCode.NOT_ELIGIBLE.getCode(), HttpStatus.NOT_FOUND,
                        ResponseMessage.NOT_ELIGIBLE.getMessage());
            }else{
                String loanReferenceId = ReferenceNumberGenerator.generate();
                Product productEnum = Product.fromString(request.getProduct());
                loan.setLoanDescription(request.getDescription())
                        .setCustomerId(account.getCustomer().getId())
                        .setAmount(new BigDecimal(request.getAmount()))
                        .setCurrency(request.getCurrency())
                        .setRequestedDate(LocalDateTime.now())
                        .setProduct(productEnum)
                        .setTenor(request.getTenor())
                        .setReferenceNo(loanReferenceId)
                        .setAccountNumber(request.getAccountNo())
                        .setAmountLiquidated(BigDecimal.ZERO)
                        .setLoanBalance(BigDecimal.ZERO)
                        .setIsLiquidated(false);
                loan = repository.save(loan);
            }
            return response.setHeaderResponse(
                    new GenericResponseDto()
                            .setResponseCode(ResponseCode.SUCCESS.getCode())
                            .setResponseMessage(ResponseMessage.TRANSACTION_SUCCESSFUL.getMessage()))
                    .setRequestedDate(loan.getRequestedDate())
                    .setAmount(loan.getAmount())
                    .setLoanReferenceId(loan.getReferenceNo());

        }else{
            throw new GenericException(ResponseCode.NOT_FOUND.getCode(), HttpStatus.NOT_FOUND,
                    ResponseMessage.INVALID_ACCOUNT_NO.getMessage());
        }
    }

    public GetLoanDetailsResponseDto getLoanDetails(String LoanReferenceId){
       Optional<Loan> loan = repository.findByReferenceNo(LoanReferenceId);
       if(loan.isEmpty()){
          throw new GenericException(ResponseCode.NOT_FOUND.getCode(),
                  HttpStatus.NOT_FOUND,
                  ResponseMessage.NO_LOAN_FOUND.getMessage());
       }else{
           GetLoanDetailsResponseDto responseDto;
           responseDto = objectMapper.map().convertValue(loan.get(), GetLoanDetailsResponseDto.class);
           log.info("Loan details returned:: "+ JsonConverter.toJson(responseDto, true));
           return responseDto;
       }
    }


    public GenericResponseDto approveLoan(ApproveOrRejectLoanRequestDto request){
     Optional<Loan> loanOptional = repository.findByReferenceNo(request.getLoanReferenceId());
        if(loanOptional.isEmpty()){
            throw new GenericException(ResponseCode.NOT_FOUND.getCode(),
                    HttpStatus.NOT_FOUND,
                    ResponseMessage.NO_LOAN_FOUND.getMessage());
        }else{
            Loan loan = loanOptional.get();
            Account account = accountRepository.findByAccountNumber(loan.getAccountNumber());
                loan.setIsApproved(true);
                loan.setIsRejected(false);
                LocalDateTime currentDateTime = LocalDateTime.now();
                int daysToAdd = Integer.parseInt(loan.getTenor());
                LocalDateTime maturityDate = currentDateTime.plusDays(daysToAdd);
                loan.setApprovedDate(currentDateTime)
                        .setLoanBalance(loan.getAmount())
                        .setMaturityDate(maturityDate);
            repository.save(loan);
            BigDecimal avlBal = account.getAvlBal() != null ? account.getAvlBal() : BigDecimal.ZERO;
            BigDecimal currBal = account.getCurrBal() != null ? account.getCurrBal() : BigDecimal.ZERO;

            account.setAvlBal(avlBal.add(loan.getAmount()));
            account.setCurrBal(currBal.add(loan.getAmount()));

            accountRepository.save(account);
        }
        return new GenericResponseDto()
                .setResponseCode(ResponseCode.SUCCESS.getCode())
                .setResponseMessage(ResponseMessage.LOAN_APPROVAL_SUCCESS.getMessage());
    }

    public GenericResponseDto rejectLoan(ApproveOrRejectLoanRequestDto request){
        Optional<Loan> loanOptional = repository.findByReferenceNo(request.getLoanReferenceId());
        if(loanOptional.isEmpty()){
            throw new GenericException(ResponseCode.NOT_FOUND.getCode(),
                    HttpStatus.NOT_FOUND,
                    ResponseMessage.NO_LOAN_FOUND.getMessage());
        }else{
            Loan loan = loanOptional.get();
            loan.setIsApproved(false);
            loan.setIsRejected(true);
            repository.save(loan);
        }
        return new GenericResponseDto()
                .setResponseCode(ResponseCode.SUCCESS.getCode())
                .setResponseMessage(ResponseMessage.LOAN_REJECTION_SUCCESS.getMessage());
    }
}
