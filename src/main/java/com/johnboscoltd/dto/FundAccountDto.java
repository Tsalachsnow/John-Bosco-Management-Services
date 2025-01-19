package com.johnboscoltd.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
public class FundAccountDto {

    @Data
    @Accessors(chain = true)
    public static class Request{
    private String amount;
    private String accountNumber;
    private String senderAccNo;
    private String senderName;
    private String descAccountNumber;
    private String debitOrCredit;
    private String narration;
    }

    @Data
    @Accessors(chain = true)
    public static class Response{
        private GenericResponseDto headerResponse;
        private String transactionRef;
    }
}
