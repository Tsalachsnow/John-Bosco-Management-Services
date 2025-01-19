package com.johnboscoltd.util;

import lombok.experimental.UtilityClass;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

@UtilityClass
public class AccountNumberGenerator {
    private static final int ACCOUNT_NUMBER_LENGTH = 10;
    private static final Set<String> generatedAccountNumbers = new HashSet<>();
    private static final Random random = new Random();

    // Method to generate a Nigerian bank account number
    public static String generateAccountNumber() {
        String accountNumber;

        do {
            accountNumber = generateRandomNumber();
        } while (!isUnique(accountNumber)); // Ensure uniqueness

        // Save the generated account number for future reference
        generatedAccountNumbers.add(accountNumber);
        return accountNumber;
    }

    // Generate a 10-digit random number
    private static String generateRandomNumber() {
        StringBuilder accountNumber = new StringBuilder();

        for (int i = 0; i < ACCOUNT_NUMBER_LENGTH; i++) {
            int digit = random.nextInt(10); // Generate a digit between 0 and 9
            accountNumber.append(digit);
        }

        return accountNumber.toString();
    }

    // Check if the account number is unique
    private static boolean isUnique(String accountNumber) {
        return !generatedAccountNumbers.contains(accountNumber);
    }

    public static void main(String[] args) {
        // Example: Generate and print 5 unique Nigerian account numbers
        for (int i = 0; i < 5; i++) {
            String accountNumber = generateAccountNumber();
            System.out.println("Generated Account Number: " + accountNumber);
        }
    }

    public static String generateAccountName(String firstName, String lastName, String otherNames){
        if(otherNames == null){
            return lastName+" "+firstName;
        }else{
            return lastName+" "+firstName+" "+otherNames;
        }
    }
}
