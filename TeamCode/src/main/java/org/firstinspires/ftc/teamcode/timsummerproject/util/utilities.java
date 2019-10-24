package org.firstinspires.ftc.teamcode.timsummerproject.util;

public class utilities {

    public char[] currentArray;

    public utilities() {
        currentArray = new char[50];
    }

    public char[] StringToChar(String string) {

        char[] Array = new char[string.length()];

        for (int i = 0; i < string.length(); i++) {
            Array[i] = string.charAt(i);
        }

        return Array;

    }
}
