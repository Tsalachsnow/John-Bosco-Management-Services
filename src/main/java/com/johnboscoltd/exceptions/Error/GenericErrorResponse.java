package com.johnboscoltd.exceptions.Error;

public class GenericErrorResponse {
        private final String responseMessage;
        private final int httpStatus;
        private final String responseCode;


        public GenericErrorResponse(String responseMessage, int httpStatus, String responseCode) {
            this.responseCode = responseCode;
            this.httpStatus = httpStatus;
            this.responseMessage = responseMessage;
        }

        // Getters
        public String getResponseCode() {
            return responseCode;
        }

        public int getHttpStatus() {
            return httpStatus;
        }

        public String getResponseMessage() {
            return responseMessage;
        }
    }

