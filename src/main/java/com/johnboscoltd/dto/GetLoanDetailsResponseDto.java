package com.johnboscoltd.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.johnboscoltd.enums.Product;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetLoanDetailsResponseDto {
    private String referenceNo;
    private String customerId;
    private BigDecimal amount;
    private String currency;
    private String loanDescription;
    private Product product;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime requestedDate;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime approvedDate;
    private Boolean isApproved;
    private Boolean isRejected;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime maturityDate;
    private String tenor;
    private BigDecimal amountLiquidated;
    private BigDecimal loanBalance;
    private Boolean isLiquidated;
}
