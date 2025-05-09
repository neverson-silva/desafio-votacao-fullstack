package com.neverson.votacao.utils;

public class CPFUtils {

    public static boolean isValid(String cpf) {
        if (cpf == null || !cpf.matches("\\d{11}") || cpf.chars().distinct().count() == 1) {
            return false;
        }

        try {
            int[] digits = cpf.chars().map(c -> c - '0').toArray();

            int sum1 = 0;
            for (int i = 0; i < 9; i++) {
                sum1 += digits[i] * (10 - i);
            }
            int firstCheckDigit = 11 - (sum1 % 11);
            if (firstCheckDigit >= 10) firstCheckDigit = 0;
            if (digits[9] != firstCheckDigit) return false;

            int sum2 = 0;
            for (int i = 0; i < 10; i++) {
                sum2 += digits[i] * (11 - i);
            }
            int secondCheckDigit = 11 - (sum2 % 11);
            if (secondCheckDigit >= 10) secondCheckDigit = 0;
            return digits[10] == secondCheckDigit;

        } catch (Exception e) {
            return false;
        }
    }
}
