package com.johnboscoltd.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transaction_history")
@Data
@Accessors(chain = true)
public class TransactionHistory {

    @Id
    @Column
    @GeneratedValue(generator = "uuid-generator")
    @GenericGenerator(name = "uuid-generator", strategy = "uuid")
    private String id;

    @Column
    private BigDecimal amount;

    @Column(name = "customer_id")
    private String customerId;

    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "trans_ref")
    private String transactionRef;

    @Column(name = "sender_account_no")
    private String senderAccNo;

    @Column(name = "sender_name")
    private String senderName;

    @Column(name = "receiver_account_no")
    private String receiverAccNo;

    @Column(name = "debit_or_credit")
    private String debitOrCredit;

    @Column(name = "tran_date")
    private LocalDateTime transactionDate;

    @Column
    private String narration;

    @Column
    private String status;

    @Column(name = "response_code")
    private String responseCode;

    @Column(name = "response_message")
    private String responseMessage;
}
