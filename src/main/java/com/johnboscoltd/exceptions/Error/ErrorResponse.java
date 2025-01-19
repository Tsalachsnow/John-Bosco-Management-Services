package com.johnboscoltd.exceptions.Error;

public class ErrorResponse {
    private final String responseMessage;
    private final String responseCode;


    public ErrorResponse(String responseMessage, String responseCode) {
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
}

