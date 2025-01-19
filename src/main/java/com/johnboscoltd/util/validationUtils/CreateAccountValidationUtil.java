package com.johnboscoltd.util.validationUtils;

import com.johnboscoltd.dto.CreateAccountDto;
import com.johnboscoltd.exceptions.GenericException;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpStatus;
import java.util.regex.Pattern;
import static com.johnboscoltd.constants.ResponseCode.*;

//@Slf4j
@UtilityClass
public class CreateAccountValidationUtil {

    public static void validateCreateAccountRequest(CreateAccountDto.Request payload) {
//        log.info("Validating Create Customer Request Payload ::::::::::::::::::");
        // Check if payload is null
        if (payload == null) {
            throw new GenericException(PAYLOAD_VALIDATOR_EXCEPTION.getCode(),
                    HttpStatus.BAD_REQUEST, "Payload cannot be null.");
        }
            if (payload.getBvn() == null || !isValidBvn(payload.getBvn())) {
                throw new GenericException(PAYLOAD_VALIDATOR_EXCEPTION.getCode(),
                        HttpStatus.BAD_REQUEST, "Invalid BVN.");
            }

            if(payload.getCountry().trim().equalsIgnoreCase("nigeria")){
                if (payload.getNin() == null || !isValidNin(payload.getNin(), payload.getCountry())) {
                    throw new GenericException(PAYLOAD_VALIDATOR_EXCEPTION.getCode(),
                            HttpStatus.BAD_REQUEST, "Invalid NIN.");
                }
            }
    }

    private static boolean isValidBvn(String bvn) {
        String bvnRegex = "^[0-9]{11}$";
        return Pattern.matches(bvnRegex, bvn);
    }

    private static boolean isValidNin(String nin, String country) {
            String bvnRegex = "^[0-9]{11}$";
            return Pattern.matches(bvnRegex, nin);
    }
}
