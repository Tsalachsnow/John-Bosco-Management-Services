package com.johnboscoltd.services;

import com.johnboscoltd.dto.CreateCustomerRequestDto;
import com.johnboscoltd.dto.GenericResponseDto;

import java.util.Optional;

public interface CustomerService {
    GenericResponseDto createCustomer(CreateCustomerRequestDto request);
}
