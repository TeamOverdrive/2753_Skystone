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

@Disabled
@TeleOp(name = "MecDrive", group = "TeleOp")
public class MecanumDrive extends LinearOpMode {

    private DcMotor motorBackLeft;
    private DcMotor motorBackRight;
    private DcMotor motorFrontLeft;
    private DcMotor motorFrontRight;

    private float redColor = 1, blueColor = 1, greenColor = 1;
    private float RGB[] = {1,1,1};
    private int colorRand = 0;

    Random rand = new Random();

    @Override

    public void runOpMode() throws InterruptedException {

        motorBackLeft = hardwareMap.dcMotor.get("left_back");
        motorBackRight = hardwareMap.dcMotor.get("right_back");
        motorFrontLeft = hardwareMap.dcMotor.get("left_front");
        motorFrontRight = hardwareMap.dcMotor.get("right_front");

        motorBackRight.setDirection(DcMotor.Direction.REVERSE);
        motorFrontRight.setDirection(DcMotor.Direction.REVERSE);

        double r;
        double robotAngle;
        double rightX;

        int relativeLayoutId = hardwareMap.appContext.getResources().getIdentifier("RelativeLayout", "id", hardwareMap.appContext.getPackageName());
        final View relativeLayout = ((Activity) hardwareMap.appContext).findViewById(relativeLayoutId);

        waitForStart();

        while(opModeIsActive()) {

            if (gamepad1.left_stick_button && gamepad1.right_stick_button) {
                if (redColor > 251 || blueColor > 251 || greenColor > 251) {
                    colorRand = rand.nextInt(3) + 1;
                    if (colorRand == 1) {
                        redColor = 250;
                        blueColor = rand.nextInt(100);
                        greenColor = rand.nextInt(100);
                    } else if (colorRand == 2) {
                        blueColor = 250;
                        redColor = rand.nextInt(100);
                        greenColor = rand.nextInt(100);
                    } else {
                        greenColor = 250;
                        blueColor = rand.nextInt(100);
                        redColor = rand.nextInt(100);
                    }
                } else {
                    if (colorRand == 1) {

                        blueColor += 0.03;
                        greenColor += 0.03;
                    } else if (colorRand == 2) {

                        redColor += 0.03;
                        greenColor += 0.03;
                    } else {

                        blueColor += 0.03;
                        redColor += 0.03;
                    }
                }

                Color.RGBToHSV((int) (redColor),
                        (int) (blueColor),
                        (int) (greenColor),
                        RGB);

                relativeLayout.post(new Runnable() {
                    public void run() {
                        relativeLayout.setBackgroundColor(Color.HSVToColor(0xff, RGB));
                    }
                });
            }

            if (!gamepad1.right_stick_button && !gamepad1.left_stick_button) {
                relativeLayout.post(new Runnable() {
                    public void run() {
                        relativeLayout.setBackgroundColor(Color.WHITE);
                    }
                });
            }

            r = Math.hypot(gamepad1.left_stick_x, gamepad1.left_stick_y);
            robotAngle = Math.atan2(gamepad1.left_stick_y, gamepad1.left_stick_x) - (Math.PI / 4);
            rightX = getStickAngle(gamepad1.right_stick_x, gamepad1.right_stick_y);
            final double v1 = r * Math.cos(robotAngle) + rightX;
            final double v2 = r * Math.sin(robotAngle) - rightX;
            final double v3 = r * Math.sin(robotAngle) + rightX;
            final double v4 = r * Math.cos(robotAngle) - rightX;

            telemetry.addData("FL Power",v1);
            telemetry.addData("FR Power",v2);
            telemetry.addData("BL Power",v3);
            telemetry.addData("BR Power",v4);
            telemetry.update();


            motorFrontLeft.setPower(v1);
            motorFrontRight.setPower(v2);
            motorBackLeft.setPower(v3);
            motorBackRight.setPower(v4);
        }

    }
    private double getStickAngle(float x, float y) {
       double returnValue =  (Math.atan2(gamepad1.left_stick_y, gamepad1.left_stick_x) - Math.PI / 2) * 2;
       if (returnValue > 1)
           returnValue = 1;
       return returnValue;
    }
}

