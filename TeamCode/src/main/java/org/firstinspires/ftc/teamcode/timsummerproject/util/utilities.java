package org.firstinspires.ftc.teamcode.timsummerproject.util;

public class utilities {

    char[] currentArray;

    public utilities() {

    }

    public void StringToChar(String string) {

        this.currentArray = new char[string.length()];
        for (int i = 0; i < string.length(); i++) {
            this.currentArray[i] = string.charAt(i);
        }

    }
}
