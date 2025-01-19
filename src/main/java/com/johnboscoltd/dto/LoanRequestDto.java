package com.johnboscoltd.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class LoanRequestDto {

    @Data
    @Accessors(chain = true)
    public static class Request{
        private String mobileNumber;
        private String email;
        private String accountNo;
        private String amount;
        private String product;
        private String description;
        private String country;
        private String currency;
        private String tenor;
    }

    @Data
    @Accessors(chain = true)
    public static class Response{
        private GenericResponseDto headerResponse;
        private String loanReferenceId;
        private BigDecimal amount;
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime requestedDate;
    }
}
