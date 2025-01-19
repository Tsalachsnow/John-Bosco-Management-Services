package com.johnboscoltd.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.security.SecureRandom;

@Slf4j
@UtilityClass
public class ReferenceNumberGenerator {
    private static final SecureRandom random = new SecureRandom();
    public String generate(){
        log.info("============== Called generate Payment Ref ===============");
        int refLenght = 12;
        StringBuilder paymentRef = new StringBuilder();
        paymentRef.append(random.nextInt(9) + 1);
        for (int i = 1; i < refLenght; i++) {
            paymentRef.append(random.nextInt(10));
        }
        return paymentRef.toString();
    }
}
