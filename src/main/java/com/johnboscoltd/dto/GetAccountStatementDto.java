package com.johnboscoltd.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class GetAccountStatementDto {

    @Data
    @Accessors(chain = true)
    public static class Request{
       private String accountNumber;
        @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Invalid date format. Expected format: yyyy-MM-dd")
       private String startDate;
        @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Invalid date format. Expected format: yyyy-MM-dd")
       private String endDate;
        private int page;
        private int size;
    }
    @Data
    @Accessors(chain = true)
    public static class Response{
    private String customerId;
    private String transactionRef;
    private BigDecimal amount;
    private String accountNumber;
    private String senderAccNo;
    private String senderName;
    private String receiverAccNo;
    private String debitOrCredit;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime transactionDate;
    private String narration;
    private String status;
    private String responseCode;
    private String responseMessage;
    }
}
