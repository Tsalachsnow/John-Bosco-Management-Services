package com.johnboscoltd.services.repositories;

import com.johnboscoltd.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, String> {
   Optional<Customer> findByMobileNumberAndEmail(String mobileNo, String email);
}
