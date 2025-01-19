package com.johnboscoltd.exceptions;

import org.springframework.http.HttpStatus;

public class GenericException extends RuntimeException {
        private final String responseCode;
        private final HttpStatus httpStatus;
        private final String responseMessage;

        // Constructor
    public GenericException(String responseCode, HttpStatus httpStatus, String responseMessage) {
            super(responseMessage); // Set the exception message
            this.responseCode = responseCode;
            this.httpStatus = httpStatus;
            this.responseMessage = responseMessage;
        }

        // Getters
        public String getResponseCode() {
            return responseCode;
        }

        public HttpStatus getHttpStatus() {
            return httpStatus;
        }

        public String getResponseMessage() {
            return responseMessage;
        }

        @Override
        public String toString() {
            return "GenericException{" +
                    "responseCode='" + responseCode + '\'' +
                    ", httpStatus=" + httpStatus +
                    ", responseMessage='" + responseMessage + '\'' +
                    '}';
        }
}
