package com.yuhtin.minecraft.wiclowpickaxes.utils;

/**
 * @author Yuhtin
 * Github: https://github.com/Yuhtin
 */
public class RomanNumbers {

    private static final String[] SYMBOLS = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};
    private static final int[] NUMBERS = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};

    public static String numural(int number) {
        for (int i = 0; i < NUMBERS.length; i++) {
            if (number >= NUMBERS[i]) {
                return SYMBOLS[i] + numural(number - NUMBERS[i]);
            }
        }

        return "";
    }

}