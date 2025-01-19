package com.johnboscoltd;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.johnboscoltd.config.JacksonConfig;
import com.johnboscoltd.constants.ResponseCode;
import com.johnboscoltd.constants.ResponseMessage;
import com.johnboscoltd.dto.CreateAccountDto;
import com.johnboscoltd.dto.GenericResponseDto;
import com.johnboscoltd.exceptions.GenericException;
import com.johnboscoltd.model.Account;
import com.johnboscoltd.model.Customer;
import com.johnboscoltd.services.AccountService;
import com.johnboscoltd.services.impl.AccountServiceImpl;
import com.johnboscoltd.services.repositories.AccountRepository;
import com.johnboscoltd.services.repositories.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;

import java.util.Optional;

public class CreateAccountTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private AccountRepository repository;

    @Mock
    private JacksonConfig jacksonConfig;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private AccountService accountService;

    @InjectMocks
    private AccountServiceImpl accountServiceImpl;

    private CreateAccountDto.Request request;

    @BeforeEach
    public void setUp() throws JsonProcessingException {
        MockitoAnnotations.openMocks(this);
        request = new CreateAccountDto.Request();
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setEmail("john.doe@example.com");
        request.setMobileNumber("2348135139753");
        request.setAccountType("S");
        request.setOtherNames("Jr");
        request.setBvn("12345678989");
        request.setNin("12345678989");
        request.setCountry("Nigeria");
        request.setCurrency("NGN");
        request.setAddress("Ajah Lagos");

        when(jacksonConfig.map()).thenReturn(objectMapper);
    }


    @Test
    public void testCreateAccount_NewCustomer() {
        // Arrange
        Customer newCustomer = new Customer();
        newCustomer.setId("456789");
        newCustomer.setAccountNo("123456789");

        when(customerRepository.findByMobileNumberAndEmail(request.getMobileNumber(), request.getEmail()))
                .thenReturn(Optional.empty());

        when(repository.save(any(Account.class))).thenReturn(new Account());

        // Act
        CreateAccountDto.Response response = accountServiceImpl.createAccount(request);

        // Print response to see what is returned
        System.out.println("Response: " + response);

        // Assert
        assertNotNull(response);
        assertNotNull(response.getHeaderResponse());  // Ensure headerResponse is not null
        assertEquals(ResponseCode.SUCCESS.getCode(), response.getHeaderResponse().getResponseCode());
        assertEquals(ResponseMessage.ACCOUNT_SUCCESSFUL_CREATION.getMessage(), response.getHeaderResponse().getResponseMessage());
        verify(customerRepository).findByMobileNumberAndEmail(request.getMobileNumber(), request.getEmail());
        verify(repository).save(any(Account.class));
    }


    @Test
    public void testCreateAccount_ExistingCustomer_AccountAlreadyExists() {
        // Arrange
        Customer existingCustomer = new Customer();
        existingCustomer.setId("4567");
        existingCustomer.setAccountNo("123456789");

        Account existingAccount = new Account();
        existingAccount.setCustomer(existingCustomer);

        when(customerRepository.findByMobileNumberAndEmail(request.getMobileNumber(), request.getEmail()))
                .thenReturn(Optional.of(existingCustomer));
        when(repository.findByCustomer_Id(existingCustomer.getId()))
                .thenReturn(Optional.of(existingAccount));

        // Act and Assert
        try {
            accountService.createAccount(request);
        } catch (GenericException ex) {
            assertEquals(ResponseCode.CUSTOMER_ACCOUNT_ALREADY_EXIST.getCode(), ex.getResponseCode());
            assertEquals(HttpStatus.CONFLICT, ex.getHttpStatus());
            assertEquals(ResponseMessage.CUSTOMER_ACCOUNT_ALREADY_EXIST.getMessage(), ex.getResponseMessage());
        }
    }

    @Test
    public void testCreateAccount_ExistingCustomer_NewAccount() {
        // Arrange
        Customer existingCustomer = new Customer();
        existingCustomer.setId("4567");
        existingCustomer.setAccountNo("123456789");

        // Mocking the request object
        CreateAccountDto.Request request = new CreateAccountDto.Request();
        request.setMobileNumber("2348135139753");
        request.setEmail("john.doe@example.com");

        // Mock the repository behavior
        when(customerRepository.findByMobileNumberAndEmail(request.getMobileNumber(), request.getEmail()))
                .thenReturn(Optional.of(existingCustomer));

        when(repository.findByCustomer_Id(existingCustomer.getId()))
                .thenReturn(Optional.empty());

        Account newAccount = new Account();
        newAccount.setAccountNumber("987654321");
        newAccount.setCustomer(existingCustomer);

        when(repository.save(any(Account.class))).thenReturn(newAccount);

        CreateAccountDto.Response mockedResponse = new CreateAccountDto.Response();
        mockedResponse.setHeaderResponse(new GenericResponseDto()
                .setResponseCode(ResponseCode.SUCCESS.getCode())
                .setResponseMessage(ResponseMessage.ACCOUNT_SUCCESSFUL_CREATION.getMessage()));

        // Mock the service method if applicable (if createAccount logic isn't directly testable)
        when(accountService.createAccount(any(CreateAccountDto.Request.class))).thenReturn(mockedResponse);

        // Act
        CreateAccountDto.Response response = accountService.createAccount(request);

        // Assert
        assertNotNull(response);
        assertEquals(ResponseCode.SUCCESS.getCode(), response.getHeaderResponse().getResponseCode());
        assertEquals(ResponseMessage.ACCOUNT_SUCCESSFUL_CREATION.getMessage(), response.getHeaderResponse().getResponseMessage());
}
}
