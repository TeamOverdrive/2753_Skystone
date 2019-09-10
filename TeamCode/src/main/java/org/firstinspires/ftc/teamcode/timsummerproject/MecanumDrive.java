package org.firstinspires.ftc.teamcode.timsummerproject;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

import java.util.Random;

public class MecanumDrive extends LinearOpMode {

    private DcMotor motorLeft;
    private DcMotor motorRight;
    private DcMotor motorFrontLeft;
    private DcMotor motorFrontRight;
    double leftStickAngle, rightStickAngle, frontRightPower = 0, frontLeftPower = 0,
            backRightPower = 0, backLeftPower = 0;

    @Override
    public void runOpMode() throws InterruptedException {

        motorLeft = hardwareMap.dcMotor.get("left_back");
        motorRight = hardwareMap.dcMotor.get("right_back");
        motorFrontLeft = hardwareMap.dcMotor.get("left_front");
        motorFrontRight = hardwareMap.dcMotor.get("right_front");

        motorRight.setDirection(DcMotor.Direction.REVERSE);
        motorFrontRight.setDirection(DcMotor.Direction.REVERSE);

        waitForStart();

        while(opModeIsActive()) {

            leftStickAngle = relativeAngle(getQuadrant(gamepad1.left_stick_x,
                    Math.abs(gamepad1.left_stick_y)), gamepad1.left_stick_x, gamepad1.left_stick_y);

            rightStickAngle = relativeAngle(getQuadrant(gamepad1.right_stick_x,
                    Math.abs(gamepad1.right_stick_y)), gamepad1.right_stick_x, gamepad1.right_stick_y);

            if (leftStickAngle == 0) {

                frontLeftPower = 1;
                backLeftPower = 1;
                backRightPower = 1;
                frontRightPower = 1;

            } else if (leftStickAngle <= 90 && leftStickAngle > 0) {

                frontLeftPower = 1;
                backLeftPower = -1 + (90 - leftStickAngle)/45;
                backRightPower = 1;
                frontRightPower = -1 + (90 - leftStickAngle)/45;

            } else if (leftStickAngle > 90 && leftStickAngle < 178) {

                frontLeftPower = 1 - (180 - leftStickAngle)/45;
                backLeftPower = -1;
                backRightPower = 1  - (180 - leftStickAngle)/45;
                frontRightPower = -1;

            } else if (leftStickAngle >= -90 && leftStickAngle < 0) {

                frontLeftPower = -1 + (90 - Math.abs(leftStickAngle))/45;
                backLeftPower = 1;
                backRightPower = -1 + (90 - Math.abs(leftStickAngle))/45;
                frontRightPower = 1;

            } else if (leftStickAngle < -90 && leftStickAngle > -178) {

                frontLeftPower = -1;
                backLeftPower = 1 - (180 - Math.abs(leftStickAngle))/45;
                backRightPower = -1;
                frontRightPower = 1 - (180 - Math.abs(leftStickAngle))/45;

            } else {
                frontLeftPower = -1;
                backLeftPower = -1;
                backRightPower = -1;
                frontRightPower = -1;
            }

            if (rightStickAngle != 0) {
                if (rightStickAngle > 0) {

                    frontLeftPower = 1;
                    backLeftPower = 1;
                    backRightPower = -1;
                    frontRightPower = -1;

                } else if (rightStickAngle < 0) {

                    frontLeftPower = -1;
                    backLeftPower = -1;
                    backRightPower = 1;
                    frontRightPower = 1;

                }
            }

            motorLeft.setPower(backLeftPower);
            motorRight.setPower(backRightPower);
            motorFrontLeft.setPower(frontLeftPower);
            motorFrontRight.setPower(frontRightPower);

        }

    }

    public int getQuadrant(float x, float y) {
        if (x > 0 && y > 0) {
            return 0;
        } else if (x <= 0 && y > 0) {
            return 1;
        } else if (x <= 0 && y <= 0) {
            return 2;
        } else {
            return 3;
        }
    }
    public double relativeAngle(int quadrant, float x, float y) {
        if (x == 0 && y <= 0) {
            return 0;
        } else if (x == 0 && y > 0) {
            return 180;
        } else if (quadrant == 0) {
            return 90 + (Math.atan(Math.abs(y)/Math.abs(x)) * 57.2957795);
        } else if (quadrant == 1) {
            return -90 - (Math.atan(Math.abs(y)/Math.abs(x)) * 57.2957795);
        } else if (quadrant == 2) {
            return -90 + (Math.atan(Math.abs(y)/Math.abs(x)) * 57.2957795);
        } else {
            return 90 - (Math.atan(Math.abs(y)/Math.abs(x)) * 57.2957795);
        }
    }
    public void angleConversion(PodInfo drive, double angle, float forwardVelocity) {
        if (angle > 135)
            angle = 135;
        if (angle < -135)
            angle = -135;
        if (angle > 0) {
            drive.left_pod = -forwardVelocity;
            drive.right_pod = -forwardVelocity + forwardVelocity*angle/180;
        }
        if (angle < 0) {
            drive.right_pod = -forwardVelocity;
            drive.left_pod = -forwardVelocity - forwardVelocity*angle/180;
        }
        if (angle == 0) {
            drive.right_pod = -forwardVelocity;
            drive.left_pod = -forwardVelocity;
        }
    }
}

