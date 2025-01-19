package com.johnboscoltd.exceptions;


public class ErrorGenericException extends RuntimeException {
    private final String responseCode;
    private final String responseMessage;

    // Constructor
    public ErrorGenericException(String responseCode, String responseMessage) {
        super(responseMessage); // Set the exception message
        this.responseCode = responseCode;
        this.responseMessage = responseMessage;
    }

    // Getters
    public String getResponseCode() {
        return responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    @Override
    public String toString() {
        return "GenericException{" +
                "responseCode='" + responseCode + '\'' +
                ", responseMessage='" + responseMessage + '\'' +
                '}';
    }
}

