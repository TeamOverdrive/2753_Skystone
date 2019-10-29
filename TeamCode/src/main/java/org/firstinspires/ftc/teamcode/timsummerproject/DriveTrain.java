package org.firstinspires.ftc.teamcode.timsummerproject;

import com.qualcomm.robotcore.hardware.DcMotor;

public class DriveTrain {
    double v1 = 0;
    double v2 = 0;
    double v3 = 0;
    double v4 = 0;

    public DriveTrain() {

    }


    public void travel(double angle, double r, double tanRight, boolean speedMaxActive) {

        if (r > 1)
            r = 1;

        double rightX = tanRight;

         this.v1 = r * Math.cos(angle) + rightX;
         this.v2 = r * Math.sin(angle) - rightX;
         this.v3 = r * Math.sin(angle) + rightX;
         this.v4 = r * Math.cos(angle) - rightX;

        if (speedMaxActive) {
            this.v1 = r * Math.cos(angle) * (2 / Math.sqrt(2)) + rightX;
            this.v2 = r * Math.sin(angle) * (2 / Math.sqrt(2)) - rightX;
            this.v3 = r * Math.sin(angle) * (2 / Math.sqrt(2)) + rightX;
            this.v4 = r * Math.cos(angle) * (2 / Math.sqrt(2)) - rightX;
        }
    }
}