package com.johnboscoltd.constants;

public enum ResponseCode {

    SUCCESS("000"),
    TRANSACTION_FAILED("800"),
    INCORRECT_EMAIL_OR_PHONE("801"),
    EMAIL_VALIDATOR_EXCEPTION("802"),
    PAYLOAD_VALIDATOR_EXCEPTION("803"),
    CUSTOMER_EXIST("804"),
    NOT_FOUND("805"),
    INCORRECT_INPUT("806"),

    CUSTOMER_ACCOUNT_ALREADY_EXIST("807"),

    NOT_ELIGIBLE("808"),

    NO_TRANSACTION_HISTORY("809");





    private final String code;
    ResponseCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
