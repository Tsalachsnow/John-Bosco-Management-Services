package com.johnboscoltd.controller;

import com.johnboscoltd.dto.CreateCustomerRequestDto;
import com.johnboscoltd.dto.GenericResponseDto;
import com.johnboscoltd.services.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/customer")
public class CustomerController {

    private final CustomerService customerService;

    @Operation(summary = "Create Customer Endpoint")
    @PostMapping(path = "/create",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public GenericResponseDto createCustomer(
            @RequestBody CreateCustomerRequestDto request) {
        return customerService.createCustomer(request);
    }
}
