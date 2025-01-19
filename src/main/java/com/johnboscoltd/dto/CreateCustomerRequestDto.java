package com.johnboscoltd.dto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors
public class CreateCustomerRequestDto {
       private String mobileNumber;
       private String email;
       private String firstName;
       private String lastName;
       private String otherNames;
       private String idType;
       private String idNumber;
       private String address;
       private String country;
       @JsonIgnore
       private String accountNo;
}
