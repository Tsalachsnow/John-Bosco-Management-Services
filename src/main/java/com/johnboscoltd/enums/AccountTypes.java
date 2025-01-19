package com.johnboscoltd.enums;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.johnboscoltd.constants.ResponseCode;
import com.johnboscoltd.exceptions.GenericException;
import com.johnboscoltd.util.AccountTypesDeserializer;
import org.springframework.http.HttpStatus;

import java.util.Arrays;

@JsonDeserialize(using = AccountTypesDeserializer.class)
public enum AccountTypes {

    SAVINGS("Savings", "S"),
    CURRENT("Current", "C");

    private final String value;
    private final String abbreviation;

    AccountTypes(String value, String abbreviation) {
        this.value = value;
        this.abbreviation = abbreviation;
    }

    public String getValue() {
        return value;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    // Method to map input to enum
    public static AccountTypes fromString(String input) {
        if (input == null || input.isBlank() || input.isEmpty()) {
           return AccountTypes.SAVINGS;
        }

        // Normalize input (e.g., ignore case and match abbreviations)
        String normalizedInput = input.trim().toUpperCase();
        return Arrays.stream(AccountTypes.values())
                .filter(accountType ->
                        accountType.name().equalsIgnoreCase(normalizedInput) ||
                                accountType.getValue().equalsIgnoreCase(input) ||
                                accountType.getAbbreviation().equalsIgnoreCase(normalizedInput))
                .findFirst()
                .orElseThrow(() ->
                        new GenericException(ResponseCode.INCORRECT_INPUT.getCode(),
                                HttpStatus.BAD_REQUEST, "Invalid Account Type "));
    }
}
