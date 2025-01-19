package com.johnboscoltd.services.repositories;

import com.johnboscoltd.model.TransactionHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;

public interface TransactionHistoryRepository extends JpaRepository<TransactionHistory, String> {
    Page<TransactionHistory> findByAccountNumberAndTransactionDateBetween(
            String accountNumber,
            LocalDateTime startDate,
            LocalDateTime endDate,
            Pageable pageable);
}
