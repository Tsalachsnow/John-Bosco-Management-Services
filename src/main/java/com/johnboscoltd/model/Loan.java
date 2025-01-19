package com.johnboscoltd.model;

import com.johnboscoltd.enums.Product;
import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "loan")
@Data
@Accessors(chain = true)
public class Loan {

    @Id
    @Column
    @GeneratedValue(generator = "uuid-generator")
    @GenericGenerator(name = "uuid-generator", strategy = "uuid")
    private String id;

    @Column(name = "reference_number")
    private String referenceNo;

    @Column(name = "customer_id")
    private String customerId;

    @Column(name = "account_number")
    private String accountNumber;

    @Column
    private BigDecimal amount;

    @Column
    private String currency;

    @Column(name = "loan_description")
    private String loanDescription;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Product product;

    @Column(name = "requested_date")
    private LocalDateTime requestedDate;

    @Column(name = "approved_date")
    private LocalDateTime approvedDate;

    @Column(name = "is_approved")
    private Boolean isApproved = false;

    @Column(name = "is_rejected")
    private Boolean isRejected = false;

    @Column(name = "maturity_date")
    private LocalDateTime maturityDate;

    @Column
    private String tenor;

    @Column(name = "amount_liquidated")
    private BigDecimal amountLiquidated;

    @Column(name = "loan_balance")
    private BigDecimal loanBalance;

    @Column(name = "is_liquidated")
    private Boolean isLiquidated;

}
