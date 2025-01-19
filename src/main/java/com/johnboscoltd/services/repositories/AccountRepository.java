package com.johnboscoltd.services.repositories;

import com.johnboscoltd.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, String>{
   Optional<Account> findByCustomer_Id(String customerId);
   Optional<Account> findById(String accountNo);

   Account findByAccountNumber(String accountNo);
}
