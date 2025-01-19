package com.johnboscoltd.constants;

public enum ResponseMessage {

    CUSTOMER_SUCCESSFUL_CREATION("Customer Created Successfully"),

    ACCOUNT_SUCCESSFUL_CREATION("Account Created Successfully"),
    CUSTOMER_ALREADY_EXIST("Customer Already Exist"),

    CUSTOMER_NOT_FOUND("Customer does not exist"),

    CUSTOMER_ACCOUNT_ALREADY_EXIST("Customer Has an Already Existing Account and Not Allowed to Create Another"),

    INVALID_AMOUNT("Invalid Amount."+"\n Amount cannot be negative"),

    INVALID_ACCOUNT_NO("Invalid Account Number"),

    TRANSACTION_SUCCESSFUL("Your Transaction has been successfully processed."),

    TRANSACTION_FAILED("Transaction Failed."),

    NOT_ELIGIBLE("Customer is Not Eligible for Another Loan At this time. Previous Loan approved is Unsettled"),

    NO_LOAN_FOUND("No loan details found for customer. Please request for Loan"),

    LOAN_APPROVAL_SUCCESS("Your Loan request has been successfully approved"),

    LOAN_REJECTION_SUCCESS("Your Loan request has been rejected."),

    INVALID_PAYLOAD("Your request payload is invalid");

    private final String message;
    ResponseMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
