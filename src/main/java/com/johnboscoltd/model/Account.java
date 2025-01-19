package com.johnboscoltd.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.johnboscoltd.enums.AccountTypes;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Entity
@Table(name = "account")
@Data
@Accessors(chain = true)
public class Account {

    @Id
    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "account_name")
    private String accountName;

    @Column(name = "account_alias")
    private String accountAlias;

    @Column
    private String currency;

    @Column(name = "account_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountTypes accountType;

    @Column(name = "active")
    private Boolean active = true;

    @Column(name = "available_balance")
    private BigDecimal avlBal;

    @Column(name = "current_balance")
    private BigDecimal currBal;

    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToOne(optional = false, fetch = FetchType.LAZY, targetEntity = Customer.class)
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    private Customer customer;
}
