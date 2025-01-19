package com.johnboscoltd.services.impl;

import com.johnboscoltd.dto.CreateCustomerRequestDto;
import com.johnboscoltd.dto.GenericResponseDto;
import com.johnboscoltd.constants.ResponseCode;
import com.johnboscoltd.constants.ResponseMessage;
import com.johnboscoltd.exceptions.GenericException;
import com.johnboscoltd.model.Customer;
import com.johnboscoltd.services.CustomerService;
import com.johnboscoltd.services.repositories.CustomerRepository;
import com.johnboscoltd.util.validationUtils.CreateCustomerValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository repository;

    @Override
    public GenericResponseDto createCustomer(CreateCustomerRequestDto request) {
        CreateCustomerValidationUtil.validateCreateCustomerRequest(request);
        Optional<Customer> customerOptional =
                repository.findByMobileNumberAndEmail(
                        request.getMobileNumber(), request.getEmail());
        if(customerOptional.isPresent()){
            System.out.println("Customer already exists!");
            throw new GenericException(ResponseCode.CUSTOMER_EXIST.getCode(),
                    HttpStatus.BAD_REQUEST,
                    ResponseMessage.CUSTOMER_ALREADY_EXIST.getMessage());
        }
        Customer customer = new Customer().setMobileNumber(request.getMobileNumber())
                .setEmail(request.getEmail())
                .setFirstName(request.getFirstName().trim().toLowerCase())
                .setLastName(request.getLastName().trim().toLowerCase())
                .setOtherNames(request.getOtherNames() == null ? "":
                        request.getOtherNames().trim().toLowerCase())
                .setCountry(request.getCountry().trim().toLowerCase())
                .setIdNumber(request.getIdNumber() == null ? "" :
                        request.getIdNumber().trim())
                .setIdType(request.getIdType() == null ? "" :
                        request.getIdType().trim().toLowerCase());
        repository.save(customer);
        return new GenericResponseDto()
                .setResponseCode(ResponseCode.SUCCESS.getCode())
                .setResponseMessage(ResponseMessage.CUSTOMER_SUCCESSFUL_CREATION.getMessage());
    }
}
