package com.johnboscoltd.dto;

import com.johnboscoltd.enums.AccountTypes;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
public class CreateAccountDto {


    @Data
    @Accessors(chain = true)
    public static class Request{
        private String mobileNumber;
        private String email;
        private String firstName;
        private String lastName;
        private String otherNames;
        private String bvn;
        private String nin;
        private String idType;
        private String idNumber;
        private String address;
        private String country;
        private String currency;
        private String accountType;
    }

    @Data
    @Accessors(chain = true)
    public static class Response{
        private GenericResponseDto headerResponse;
        private String accountName;
        private AccountTypes accountType;
        private String accountNumber;
    }
}
