package com.johnboscoltd.util.validationUtils;

import com.johnboscoltd.dto.CreateCustomerRequestDto;
import com.johnboscoltd.constants.ResponseCode;
import com.johnboscoltd.exceptions.GenericException;
import com.johnboscoltd.model.Customer;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import java.util.regex.Pattern;

import static com.johnboscoltd.constants.ResponseCode.*;

@Slf4j
@UtilityClass
public class CreateCustomerValidationUtil {
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";

    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
    public boolean isValidEmail(String email) {

        if (email == null || email.isEmpty()) {
            throw new GenericException("Email Cannot be null or empty",
                    HttpStatus.BAD_REQUEST,
                    ResponseCode.EMAIL_VALIDATOR_EXCEPTION.getCode());
        }
        if(!EMAIL_PATTERN.matcher(email).matches()){
            throw new GenericException("Invalid Email Address",
                    HttpStatus.BAD_REQUEST,
                    ResponseCode.EMAIL_VALIDATOR_EXCEPTION.getCode());
        }
        return true;
    }

    public static void validateCreateCustomerRequest(CreateCustomerRequestDto payload) {
//        log.info("Validating Create Customer Request Payload ::::::::::::::::::");
        // Check if payload is null
        if (payload == null) {
            throw new GenericException(PAYLOAD_VALIDATOR_EXCEPTION.getCode(),
                    HttpStatus.BAD_REQUEST, "Payload cannot be null.");
        }
        // Validate firstname
        if (payload.getFirstName() == null || payload.getFirstName().trim().isEmpty()) {
            throw new GenericException(PAYLOAD_VALIDATOR_EXCEPTION.getCode(),
                    HttpStatus.BAD_REQUEST, "Firstname cannot be null or empty.");
        }

        // Validate lastname
        if (payload.getLastName() == null || payload.getLastName().trim().isEmpty()) {
            throw new GenericException(PAYLOAD_VALIDATOR_EXCEPTION.getCode(),
                    HttpStatus.BAD_REQUEST, "Lastname cannot be null or empty.");
        }

        if (payload.getAddress() == null || payload.getAddress().trim().isEmpty()) {
            throw new GenericException(PAYLOAD_VALIDATOR_EXCEPTION.getCode(),
                    HttpStatus.BAD_REQUEST, "Customer Address cannot be null or empty.");
        }

        // Validate email
        if (payload.getEmail() == null || !isValidEmail(payload.getEmail())) {
            throw new GenericException(PAYLOAD_VALIDATOR_EXCEPTION.getCode(),
                    HttpStatus.BAD_REQUEST, "Invalid email address.");
        }

        // Validate mobile number
        if (payload.getMobileNumber() == null || !isValidMobileNumber(payload.getMobileNumber())) {
            throw new GenericException(PAYLOAD_VALIDATOR_EXCEPTION.getCode(),
                    HttpStatus.BAD_REQUEST, "Invalid mobile number.");
        }

        // Validate country
        if (payload.getCountry() == null || payload.getCountry().trim().isEmpty()) {
            throw new GenericException(PAYLOAD_VALIDATOR_EXCEPTION.getCode(),
                    HttpStatus.BAD_REQUEST, "Country cannot be null or empty.");
        }

    }


    public static void validateCreateCustomerRequest(Customer payload) {
//        log.info("Validating Create Customer Request Payload ::::::::::::::::::");
        // Check if payload is null
        if (payload == null) {
            throw new GenericException(PAYLOAD_VALIDATOR_EXCEPTION.getCode(),
                    HttpStatus.BAD_REQUEST, "Payload cannot be null.");
        }
        // Validate firstname
        if (payload.getFirstName() == null || payload.getFirstName().trim().isEmpty()) {
            throw new GenericException(PAYLOAD_VALIDATOR_EXCEPTION.getCode(),
                    HttpStatus.BAD_REQUEST, "Firstname cannot be null or empty.");
        }

        // Validate lastname
        if (payload.getLastName() == null || payload.getLastName().trim().isEmpty()) {
            throw new GenericException(PAYLOAD_VALIDATOR_EXCEPTION.getCode(),
                    HttpStatus.BAD_REQUEST, "Lastname cannot be null or empty.");
        }

        if (payload.getAddress() == null || payload.getAddress().trim().isEmpty()) {
            throw new GenericException(PAYLOAD_VALIDATOR_EXCEPTION.getCode(),
                    HttpStatus.BAD_REQUEST, "Customer Address cannot be null or empty.");
        }

        // Validate email
        if (payload.getEmail() == null || !isValidEmail(payload.getEmail())) {
            throw new GenericException(PAYLOAD_VALIDATOR_EXCEPTION.getCode(),
                    HttpStatus.BAD_REQUEST, "Invalid email address.");
        }

        // Validate mobile number
        if (payload.getMobileNumber() == null || !isValidMobileNumber(payload.getMobileNumber())) {
            throw new GenericException(PAYLOAD_VALIDATOR_EXCEPTION.getCode(),
                    HttpStatus.BAD_REQUEST, "Invalid mobile number.");
        }

        // Validate country
        if (payload.getCountry() == null || payload.getCountry().trim().isEmpty()) {
            throw new GenericException(PAYLOAD_VALIDATOR_EXCEPTION.getCode(),
                    HttpStatus.BAD_REQUEST, "Country cannot be null or empty.");
        }

    }

    // Helper method to validate mobile number (example for a generic 10-digit number)
    private static boolean isValidMobileNumber(String mobileNumber) {
        String mobileRegex;
        if(mobileNumber.startsWith("234")){
            mobileNumber = mobileNumber.substring(3, mobileNumber.length());
            mobileRegex = "^[0-9]{10}$";
        }else{
            mobileRegex = "^[0-9]{11}$";
        }

        return Pattern.matches(mobileRegex, mobileNumber);
    }
}
