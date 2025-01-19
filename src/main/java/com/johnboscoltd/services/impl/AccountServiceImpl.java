package com.johnboscoltd.services.impl;

import com.johnboscoltd.config.JacksonConfig;
import com.johnboscoltd.dto.CreateAccountDto;
import com.johnboscoltd.dto.FundAccountDto;
import com.johnboscoltd.dto.GenericResponseDto;
import com.johnboscoltd.dto.GetAccountStatementDto;
import com.johnboscoltd.enums.AccountTypes;
import com.johnboscoltd.constants.ResponseCode;
import com.johnboscoltd.constants.ResponseMessage;
import com.johnboscoltd.exceptions.ErrorGenericException;
import com.johnboscoltd.exceptions.GenericException;
import com.johnboscoltd.model.Account;
import com.johnboscoltd.model.Customer;
import com.johnboscoltd.model.TransactionHistory;
import com.johnboscoltd.services.AccountService;
import com.johnboscoltd.services.repositories.AccountRepository;
import com.johnboscoltd.services.repositories.CustomerRepository;

import static com.johnboscoltd.util.AccountNumberGenerator.*;
import static com.johnboscoltd.util.validationUtils.CreateCustomerValidationUtil.*;

import com.johnboscoltd.services.repositories.TransactionHistoryRepository;
import com.johnboscoltd.util.ReferenceNumberGenerator;
import com.johnboscoltd.util.transformers.AccountRequestDtoToCustomerTransformer;
import com.johnboscoltd.util.validationUtils.CreateAccountValidationUtil;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final CustomerRepository customerRepository;
    private final AccountRepository repository;
    private final TransactionHistoryRepository transactionRepository;
    private final JacksonConfig objectMapper;

    @Override
    public CreateAccountDto.Response createAccount(CreateAccountDto.Request request) {
        GenericResponseDto response = new GenericResponseDto();
        CreateAccountValidationUtil.validateCreateAccountRequest(request);
        String accountNumber = generateAccountNumber();
        Customer customer;

        Optional<Customer> customerOptional =
                customerRepository.findByMobileNumberAndEmail(request.getMobileNumber(), request.getEmail());

        if (customerOptional.isPresent()) {
            customer = customerOptional.get();
            Optional<Account> accountOptional = repository.findByCustomer_Id(customer.getId());
            if (accountOptional.isPresent()) {
                throw new GenericException(ResponseCode.CUSTOMER_ACCOUNT_ALREADY_EXIST.getCode(),
                        HttpStatus.CONFLICT, ResponseMessage.CUSTOMER_ACCOUNT_ALREADY_EXIST.getMessage());
            }
            customer = createAccountForExistingCustomer(request, customerOptional.get(), accountNumber);
        } else {
            customer = createAccountForNewCustomer(request, accountNumber);
        }
        String accountName = generateAccountName(
                request.getFirstName(), request.getLastName(), request.getOtherNames());
        AccountTypes accountType = AccountTypes.fromString(request.getAccountType());
        Account account = new Account();
        account.setCurrency(request.getCurrency());
//        account = objectMapper.map().convertValue(request, Account.class);
        account.setAccountNumber(customer.getAccountNo())
                .setAccountType(accountType)
                .setAccountName(accountName)
                .setAccountAlias(request.getMobileNumber())
                .setActive(true).setCustomer(customer);
        account = repository.save(account);

        response.setResponseCode(ResponseCode.SUCCESS.getCode())
                .setResponseMessage(ResponseMessage.ACCOUNT_SUCCESSFUL_CREATION.getMessage());
        CreateAccountDto.Response dtoResponse = new CreateAccountDto.Response()
                .setHeaderResponse(response)
                .setAccountName(account.getAccountName())
                .setAccountNumber(account.getAccountNumber())
                .setAccountType(account.getAccountType());
        System.out.println("Returning response: " + dtoResponse);
        return dtoResponse;
    }

    public Customer createAccountForExistingCustomer(CreateAccountDto.Request request,
                                                     Customer customer, String accountNumber) {
        customer.setAccountNo(accountNumber);
        customer = AccountRequestDtoToCustomerTransformer.transform(request, customer);
        customer = customerRepository.save(customer);
        return customer;
    }

//    @Transactional
    public Customer createAccountForNewCustomer(CreateAccountDto.Request request, String accountNumber) {
        log.info("i am here:::::::::::::::::::::::::");
        log.info("Request before transformation: {}", request);
        request = AccountRequestDtoToCustomerTransformer.transform(request);
        log.info("after transformation:: " + request);
        Customer customer = AccountRequestDtoToCustomerTransformer.transformRequest(request);
        customer.setAccountNo(accountNumber);
        log.info("after mapper::::: " + customer);
        validateCreateCustomerRequest(customer);
        System.out.println("Customer:: "+customer);
        try{
        customerRepository.save(customer);
        }catch (Exception e){
          log.info("customer creation failed:: ");
        }
        return customer;
    }


    public FundAccountDto.Response fundAccount(FundAccountDto.Request request) {
        TransactionHistory transactionHistory = new TransactionHistory();
        FundAccountDto.Response response = new FundAccountDto.Response();
        GenericResponseDto responseDto = new GenericResponseDto();
        Optional<Account> accountOptional = repository.findById(request.getAccountNumber());
        if (accountOptional.isEmpty()) {
            throw new GenericException(ResponseCode.NOT_FOUND.getCode(),
                    HttpStatus.NOT_FOUND, ResponseMessage.INVALID_ACCOUNT_NO.getMessage());
        }
        Account account = accountOptional.get();
        BigDecimal amount = validateAmount(request.getAmount());
        String paymentRef = ReferenceNumberGenerator.generate();

        BigDecimal avlBal = account.getAvlBal() != null ? account.getAvlBal() : BigDecimal.ZERO;
        BigDecimal currBal = account.getCurrBal() != null ? account.getCurrBal() : BigDecimal.ZERO;

        account.setAvlBal(avlBal.add(amount));
        account.setCurrBal(currBal.add(amount));

        transactionHistory.setAmount(amount).setNarration(request.getNarration())
                .setDebitOrCredit(request.getDebitOrCredit())
                .setAccountNumber(request.getAccountNumber())
                .setReceiverAccNo(request.getDebitOrCredit().equalsIgnoreCase("Cr") ?
                        request.getAccountNumber() :
                        request.getDescAccountNumber())
                .setTransactionRef(paymentRef)
                .setSenderAccNo(request.getSenderAccNo())
                .setSenderName(request.getSenderName())
                .setCustomerId(account.getCustomer().getId())
                .setTransactionDate(LocalDateTime.now());

        try {
            account = repository.save(account);
            transactionHistory.setStatus("SUCCESS")
                    .setResponseCode(ResponseCode.SUCCESS.getCode())
                    .setResponseMessage(ResponseMessage.TRANSACTION_SUCCESSFUL.getMessage());
            responseDto.setResponseCode(ResponseCode.SUCCESS.getCode())
                    .setResponseMessage(ResponseMessage.TRANSACTION_SUCCESSFUL.getMessage());
            transactionRepository.save(transactionHistory);

        } catch (Exception e) {
            transactionHistory.setStatus("FAILED")
                    .setResponseCode(ResponseCode.TRANSACTION_FAILED.getCode())
                    .setResponseMessage(ResponseMessage.TRANSACTION_FAILED.getMessage());
            responseDto.setResponseCode(ResponseCode.TRANSACTION_FAILED.getCode())
                    .setResponseMessage(ResponseMessage.TRANSACTION_FAILED.getMessage());
            transactionRepository.save(transactionHistory);
            throw e;
        }
        return response.setHeaderResponse(responseDto)
                .setTransactionRef(paymentRef);
    }


    public Page<GetAccountStatementDto.Response> getAccountStatement(GetAccountStatementDto.Request request) {
        Optional<Account> accountOptional = repository.findById(request.getAccountNumber());
        if (accountOptional.isEmpty()) {
            throw new GenericException(ResponseCode.NOT_FOUND.getCode(),
                    HttpStatus.NOT_FOUND, ResponseMessage.INVALID_ACCOUNT_NO.getMessage());
        }
        LocalDate start = LocalDate.parse(request.getStartDate()); // YYYY-MM-DD
        LocalDate end = LocalDate.parse(request.getEndDate());

        // Converting to LocalDateTime for the start of the day and end of the day
        LocalDateTime startDateTime = start.atStartOfDay();
        LocalDateTime endDateTime = end.atTime(23, 59, 59);

        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());

        return mapTo(transactionRepository.findByAccountNumberAndTransactionDateBetween(
                request.getAccountNumber(), startDateTime, endDateTime, pageable));

    }

    public static BigDecimal validateAmount(String amountString) {
        BigDecimal amount;
        if (amountString == null || amountString.trim().isEmpty()) {
            throw new IllegalArgumentException("Amount cannot be null or empty");
        }

        try {
            amount = new BigDecimal(amountString);

            if (amount.compareTo(BigDecimal.ZERO) < 0) {
                throw new GenericException(ResponseCode.TRANSACTION_FAILED.getCode(),
                        HttpStatus.NOT_ACCEPTABLE, ResponseMessage.INVALID_AMOUNT.getMessage());
            }
        } catch (NumberFormatException e) {
            throw new GenericException(ResponseCode.TRANSACTION_FAILED.getCode(),
                    HttpStatus.NOT_ACCEPTABLE, "Invalid Amount format.");
        }
        return amount;
    }

    public Page<GetAccountStatementDto.Response> mapTo(Page<TransactionHistory> transactionHistoryPage) {
        if (transactionHistoryPage == null || transactionHistoryPage.isEmpty()) {
            throw new GenericException(ResponseCode.NO_TRANSACTION_HISTORY.getCode(), HttpStatus.OK, "No Transaction History found");
        }
        return transactionHistoryPage.map(transactionHistory -> {
            GetAccountStatementDto.Response response = new GetAccountStatementDto.Response();
            response.setCustomerId(transactionHistory.getCustomerId());
            response.setAccountNumber(transactionHistory.getAccountNumber());
            response.setAmount(transactionHistory.getAmount());
            response.setTransactionDate(transactionHistory.getTransactionDate()); // Formatting if needed
            response.setDebitOrCredit(transactionHistory.getDebitOrCredit());
            response.setNarration(transactionHistory.getNarration());
            response.setStatus(transactionHistory.getStatus());
            response.setSenderName(transactionHistory.getSenderName());
            response.setSenderAccNo(transactionHistory.getSenderAccNo());
            response.setReceiverAccNo(transactionHistory.getReceiverAccNo());
            response.setTransactionRef(transactionHistory.getTransactionRef());
            response.setResponseCode(transactionHistory.getResponseCode());
            response.setResponseMessage(transactionHistory.getResponseMessage());
            return response;
        });
    }

}
