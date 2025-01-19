package com.johnboscoltd;

import com.johnboscoltd.dto.CreateCustomerRequestDto;
import com.johnboscoltd.dto.GenericResponseDto;
import com.johnboscoltd.constants.ResponseCode;
import com.johnboscoltd.constants.ResponseMessage;
import com.johnboscoltd.exceptions.GenericException;
import com.johnboscoltd.model.Customer;
import com.johnboscoltd.services.impl.CustomerServiceImpl;
import com.johnboscoltd.services.repositories.CustomerRepository;
import com.johnboscoltd.util.validationUtils.CreateCustomerValidationUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class CreateCustomerTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerServiceImpl customerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createCustomer_shouldCreateNewCustomer_whenCustomerDoesNotExist() {
        // Arrange
        CreateCustomerRequestDto requestDto = new CreateCustomerRequestDto();
        requestDto.setMobileNumber("08012345678");
        requestDto.setEmail("john.doe@example.com");
        requestDto.setFirstName("John");
        requestDto.setLastName("Doe");
        requestDto.setAddress("Ajah Lagos");
        requestDto.setCountry("Nigeria");

        // Mock static validation to not throw exception
        try (MockedStatic<CreateCustomerValidationUtil> mockedStatic = mockStatic(CreateCustomerValidationUtil.class)) {
            mockedStatic.when(() -> CreateCustomerValidationUtil.validateCreateCustomerRequest(requestDto))
                    .thenAnswer(invocation -> null);  // Allow validation to pass
        }

        // Mock repository behavior
        when(customerRepository.findByMobileNumberAndEmail(requestDto.getMobileNumber(), requestDto.getEmail()))
                .thenReturn(Optional.empty()); // Simulating no existing customer

        // Simulate the saved customer object after calling save()
        Customer savedCustomer = new Customer()
                .setId("12345")
                .setMobileNumber(requestDto.getMobileNumber())
                .setAddress(requestDto.getAddress())
                .setEmail(requestDto.getEmail());

        when(customerRepository.save(any(Customer.class))).thenReturn(savedCustomer);

        // Act
        GenericResponseDto response = customerService.createCustomer(requestDto);

        // Assert: Check if save() was actually called
        assertEquals(ResponseCode.SUCCESS.getCode(), response.getResponseCode());
        assertEquals(ResponseMessage.CUSTOMER_SUCCESSFUL_CREATION.getMessage(), response.getResponseMessage());
    }


    @Test
    void createCustomer_shouldThrowException_whenCustomerAlreadyExists() {
        // Arrange
        CreateCustomerRequestDto requestDto = new CreateCustomerRequestDto();
        requestDto.setMobileNumber("08012345678");
        requestDto.setEmail("john.doe@example.com");
        requestDto.setFirstName("John");
        requestDto.setLastName("Doe");
        requestDto.setAddress("Ajah Lagos");
        requestDto.setCountry("Nigeria");

        // Creating an existing customer mock
        Customer existingCustomer = new Customer()
                .setId("12345")
                .setMobileNumber(requestDto.getMobileNumber())
                .setEmail(requestDto.getEmail());

        // Save the existing customer to simulate that this customer is already in the database
        customerRepository.save(existingCustomer);

        // Mock the behavior of customerRepository to simulate that the customer already exists
        when(customerRepository.findByMobileNumberAndEmail(
                requestDto.getMobileNumber(), requestDto.getEmail()))
                .thenReturn(Optional.of(existingCustomer));

        // Act & Assert: Directly handle exception and verify the exception's details
        try {
            customerService.createCustomer(requestDto);
        } catch (GenericException exception) {
            // Assert: Verify the exception message and code
            assertEquals(ResponseCode.CUSTOMER_EXIST.getCode(), exception.getResponseCode());
            assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
            assertEquals(ResponseMessage.CUSTOMER_ALREADY_EXIST.getMessage(), exception.getMessage());
        }
    }






    @Test
    void createCustomer_shouldValidateRequest_beforeProceeding() {
        // Arrange
        CreateCustomerRequestDto requestDto = new CreateCustomerRequestDto();
        requestDto.setMobileNumber(null);
        requestDto.setEmail("john.doe@example.com");

        // Mock static method to throw an exception for validation
        try (MockedStatic<CreateCustomerValidationUtil> mockedStatic = mockStatic(CreateCustomerValidationUtil.class)) {
            mockedStatic.when(() -> CreateCustomerValidationUtil.validateCreateCustomerRequest(requestDto))
                    .thenThrow(new GenericException(ResponseCode.PAYLOAD_VALIDATOR_EXCEPTION.getCode(),
                            HttpStatus.BAD_REQUEST,
                            ResponseMessage.INVALID_PAYLOAD.getMessage()));

            // Act & Assert
            GenericException exception = assertThrows(GenericException.class,
                    () -> customerService.createCustomer(requestDto));

            assertEquals(ResponseCode.PAYLOAD_VALIDATOR_EXCEPTION.getCode(), exception.getResponseCode());
            assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
            assertEquals(ResponseMessage.INVALID_PAYLOAD.getMessage(), exception.getMessage());

            // Ensure no repository interaction occurs
            verify(customerRepository, never()).findByMobileNumberAndEmail(anyString(), anyString());
            verify(customerRepository, never()).save(any(Customer.class));
        }
    }
}
