package org.firstinspires.ftc.teamcode.timsummerproject.subsystems;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

public class DriveTrain {

    public double FrontLeft = 0;
    public double FrontRight = 0;
    public double BackLeft = 0;
    public double BackRight = 0;


    public DriveTrain() {

    }

    public void move(double angle, double r, double tanRight) {

        if (r > 1)
            r = 1;

        this.FrontLeft = r * Math.cos(angle) * (2 / Math.sqrt(2)) + tanRight;
        this.FrontRight = r * Math.sin(angle) * (2 / Math.sqrt(2)) - tanRight;
        this.BackLeft = r * Math.sin(angle) * (2 / Math.sqrt(2)) + tanRight;
        this.BackRight = r * Math.cos(angle) * (2 / Math.sqrt(2)) - tanRight;

    }

    public void move(String leftRight,float speed) {

        if (leftRight.equalsIgnoreCase("RIGHT")) {
            this.FrontLeft = Math.abs(speed);
            this.FrontRight = -Math.abs(speed);
            this.BackLeft = -Math.abs(speed);
            this.BackRight = Math.abs(speed);
        }

        if (leftRight.equalsIgnoreCase("LEFT")) {
            this.FrontLeft = -Math.abs(speed);
            this.FrontRight = Math.abs(speed);
            this.BackLeft = Math.abs(speed);
            this.BackRight = -Math.abs(speed);
        }
    }

    public void move(float speed) {

        this.FrontLeft = speed;
        this.FrontRight = speed;
        this.BackLeft = speed;
        this.BackRight = speed;

    }

}