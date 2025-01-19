package com.johnboscoltd.util.transformers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.johnboscoltd.dto.CreateAccountDto;
import com.johnboscoltd.dto.CreateCustomerRequestDto;
import com.johnboscoltd.model.Customer;
import lombok.experimental.UtilityClass;

@UtilityClass
public class AccountRequestDtoToCustomerTransformer {


    public Customer transform(CreateAccountDto.Request request, Customer customer) {
        if (request.getEmail() != null) {
            if (!request.getEmail().isEmpty()) {
                customer.setEmail(request.getEmail().trim().toLowerCase());
            }
        }
        if (request.getFirstName() != null) {
            if (!request.getFirstName().isEmpty()) {
                customer.setFirstName(request.getFirstName().trim().toLowerCase());
            }
        }
        if (request.getLastName() != null) {
            if (!request.getLastName().isEmpty()) {
                customer.setLastName(request.getLastName().trim().toLowerCase());
            }
        }
        if (request.getAddress() != null) {
            if (!request.getAddress().isEmpty()) {
                customer.setAddress(request.getAddress());
            }
        }
        if (request.getCountry() != null) {
            if (!request.getCountry().isEmpty()) {
                customer.setCountry(request.getCountry().trim().toLowerCase());
            }
        }

        if (request.getIdType() != null) {
            if (!request.getIdType().isEmpty()) {
                customer.setIdType(request.getIdType());
            }
        }

        if (request.getIdNumber() != null) {
            if (!request.getIdNumber().isEmpty()) {
                customer.setIdNumber(request.getIdNumber());
            }
        }
        customer.setBvn(request.getBvn())
                .setNin(request.getNin());
    return customer;
}


public CreateAccountDto.Request transform(CreateAccountDto.Request request){
    request.setEmail(request.getEmail().trim().toLowerCase());
    request.setCountry(request.getCountry().trim().toLowerCase());
    request.setCurrency(request.getCurrency().trim().toUpperCase());
    request.setAddress(request.getAddress().toLowerCase());
    request.setLastName(request.getLastName().trim().toLowerCase());
    request.setFirstName(request.getFirstName().trim().toLowerCase());
    request.setOtherNames(request.getOtherNames() == null? "":
            request.getOtherNames().trim().toLowerCase());
return request;
    }

    public Customer transformRequest(CreateAccountDto.Request request){
        return new Customer()
                .setFirstName(request.getFirstName())
                .setLastName(request.getLastName())
                .setOtherNames(request.getOtherNames())
                .setBvn(request.getBvn())
                .setNin(request.getNin())
                .setAddress(request.getAddress())
                .setEmail(request.getEmail())
                .setMobileNumber(request.getMobileNumber())
                .setIdNumber(request.getIdNumber())
                .setIdType(request.getIdType())
                .setCountry(request.getCountry());
    }
}
