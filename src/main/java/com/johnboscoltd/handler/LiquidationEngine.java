package com.johnboscoltd.handler;


import com.johnboscoltd.constants.ResponseCode;
import com.johnboscoltd.constants.ResponseMessage;
import com.johnboscoltd.model.Account;
import com.johnboscoltd.model.Loan;
import com.johnboscoltd.model.TransactionHistory;
import com.johnboscoltd.services.repositories.AccountRepository;
import com.johnboscoltd.services.repositories.LoanRepository;
import com.johnboscoltd.services.repositories.TransactionHistoryRepository;
import com.johnboscoltd.util.ReferenceNumberGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class LiquidationEngine {

    private final AccountRepository accountRepository;
    private final LoanRepository loanRepository;
    private final TransactionHistoryRepository transactionHistoryRepository;

//    @Scheduled(cron = "0 0 0 3 * *", zone = "UTC")
    @Scheduled(fixedRate = 120000) // Executes every 2 minutes (120,000 ms)
    public void debitLoanAmount() {
        log.info("Starting scheduled task to debit loan amounts...");

        List<Loan> loans = loanRepository.findAllByIsApproved(true);
        for (Loan loan : loans) {
            try {
                Account account = accountRepository.findByAccountNumber(loan.getAccountNumber());
                BigDecimal avlBal = account.getAvlBal() != null ? account.getAvlBal() : BigDecimal.ZERO;
                BigDecimal currBal = account.getCurrBal() != null ? account.getCurrBal() : BigDecimal.ZERO;

                BigDecimal loanAmount = loan.getAmount();
                String product = loan.getProduct().getValue();
                double percentageToDebit = Double.parseDouble(product) / 100;
                BigDecimal debitAmount = loanAmount.multiply(BigDecimal.valueOf(percentageToDebit));

                // Check if account has sufficient balance
                if (avlBal.compareTo(debitAmount) >= 0) {
                    // Deduct the amount
                    account.setAvlBal(avlBal.subtract(debitAmount));
                    account.setCurrBal(currBal.subtract(debitAmount));

                    // Save the updated account
                    accountRepository.save(account);

                    // Record the transaction
                    TransactionHistory transaction = new TransactionHistory();
                    transaction.setSenderAccNo(account.getAccountNumber())
                            .setAmount(debitAmount)
                            .setNarration("Loan repayment - 10% deduction")
                            .setTransactionDate(LocalDateTime.now())
                            .setStatus("SUCCESS")
                            .setDebitOrCredit("Dr")
                            .setAccountNumber(account.getAccountNumber())
                            .setTransactionRef(ReferenceNumberGenerator.generate())
                            .setCustomerId(account.getCustomer().getId())
                            .setSenderName(account.getAccountName())
                            .setResponseCode(ResponseCode.SUCCESS.getCode())
                            .setResponseMessage(ResponseMessage.TRANSACTION_SUCCESSFUL.getMessage());
                    transactionHistoryRepository.save(transaction);
                    loan.setLoanBalance(loanAmount.subtract(debitAmount));
                    BigDecimal amountLiquidated = loan.getAmountLiquidated() != null ? loan.getAmountLiquidated() : BigDecimal.ZERO;
                    loan.setAmountLiquidated(amountLiquidated.add(debitAmount));
                    if(amountLiquidated.compareTo(loanAmount) == 0){
                        loan.setIsLiquidated(true);
                    }
                    loanRepository.save(loan);

                    log.info("Successfully debited 10% of loan amount for account: {}", account.getAccountNumber());
                } else {
                    log.warn("Insufficient balance for account: {}", account.getAccountNumber());
                }
            } catch (Exception e) {
                log.error("Error processing loan deduction for loan ID: {}", loan.getId(), e);
            }
        }

        log.info("Scheduled task completed.");
    }
}
