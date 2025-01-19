package com.johnboscoltd.services.repositories;

import com.johnboscoltd.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LoanRepository extends JpaRepository<Loan, String> {
    Optional<Loan> findByCustomerIdAndIsApprovedAndIsLiquidated(String customerId, Boolean isApproved, Boolean isLiquidated);
    Optional<Loan> findByReferenceNo(String refNo);
    List<Loan> findAllByIsApproved(Boolean isApproved);
}
