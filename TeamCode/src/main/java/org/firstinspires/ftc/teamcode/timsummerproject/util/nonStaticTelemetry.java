package org.firstinspires.ftc.teamcode.timsummerproject.util;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class nonStaticTelemetry {

    public Telemetry telemetry;
    public char[][] output = new char[20][50], override = new char[20][];
    boolean[] SB = new boolean[20];
    int side = 0, up = 0;

    public String runMode = "PRINT";

    utilities util = new utilities();
    public nonStaticTelemetry(Telemetry telemetryImport) {
        telemetry = telemetryImport;
        for(int i = 0; i < SB.length; i++) {
            SB[i] = false;
        }
        for (int i = 0; i < 20; i++) {
            for (int k = 0; k < 50; k++) {
                override[i][k] = ' ';
            }
        }
    }
    public void setLine(String input, int line) {
        this.util.StringToChar(input);
        this.output[line] = this.util.currentArray;
    }
    public void setBulkData(String[] input) {
        for (int i = 0; i < input.length; i++) {
            this.util.StringToChar(input[i]);
            this.output[i] = this.util.currentArray;
        }
    }
    public void setRunMode(String mode) {
        if (mode.equalsIgnoreCase("PRINT"))
            this.runMode = "PRINT";
        if (mode.equalsIgnoreCase("SCROLL"))
            this.runMode = "SCROLL";

    }
    public void setScrollLine(int line, boolean t) {

        this.SB[line] = t;

    }
    public void print() {
        String[] systemOut = new String[20];
        for(int i = 0; i < 20; i++) {
            if (!SB[i]) {
                systemOut[i] = String.valueOf(this.getOverride(this.output[i],i));
            }
        }
        if (this.runMode.equals("PRINT")) {
            for (int p = 0; p < 20; p++) {
                if (this.SB[p]) {
                    this.telemetry.addLine(this.getScroll(this.output[p],p));
                } else {
                    this.telemetry.addLine(systemOut[p]);
                }

            }

        }
        if (this.runMode.equals("SCROLL")) {
            char[][] ScrollUpArrayMod = new char[20][];
            for (int i = 0; i < ScrollUpArrayMod.length; i++) {
                ScrollUpArrayMod[i] = this.getOverride(this.output[i + this.up],i);
                this.telemetry.addLine(String.valueOf(ScrollUpArrayMod[i]));
            }

        }
        this.telemetry.update();
    }
    private String getScroll(char[] input, int line) {

        char[] outputArray = new char[20];
        for (int i = 0; i < outputArray.length; i++) {
            outputArray[i] = input[i + this.side];
        }
        return String.valueOf(this.getOverride(outputArray,line));
    }
    private char[] getOverride(char[] input, int lineCheck) {
        char[] output = new char[50];
        for (int i = 0; i < 50; i++) {
            if (!(this.override[lineCheck][i] == ' ')) {
                if (this.override[lineCheck][i] == '*') {
                    output[i] = ' ';
                } else {
                    output[i] = this.override[lineCheck][i];
                }
            } else {
                output[i] = input[i];
            }
        }
        return output;
    }
    public void setPopUp(String line1,String line2, String line3, String line4, int x, int y) {
        char[] output = new char[50];

    }
}
