package org.firstinspires.ftc.teamcode.timsummerproject;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

import java.util.Random;


import java.util.Random;

@TeleOp(name = "MecDrive2.0", group = "TeleOp")
public class MecanumDrive2 extends LinearOpMode {

    private float redColor = 1, blueColor = 1, greenColor = 1;

    private float RGB[] = {1,1,1};

    private DcMotor motorBackLeft;
    private DcMotor motorBackRight;
    private DcMotor motorFrontLeft;
    private DcMotor motorFrontRight;

    boolean xButtonNotDown = false;
    boolean yButtonNotDown = false;
    boolean aButtonNotDown = false;
    boolean bButtonNotDown = false;
    boolean frontEnd = true;
    boolean doMaxSpeed = true;
    boolean doAddDisplay = false;

    int opMode = 0;
    private int colorRand = 0;

    double relativeAngle;

    BNO055IMU imu;

    Orientation angles;
    Acceleration gravity;

    Random rand = new Random();

    @Override
    public void runOpMode() throws InterruptedException {

        motorBackLeft = hardwareMap.dcMotor.get("left_back");
        motorBackRight = hardwareMap.dcMotor.get("right_back");
        motorFrontLeft = hardwareMap.dcMotor.get("left_front");
        motorFrontRight = hardwareMap.dcMotor.get("right_front");

        motorBackLeft.setDirection(DcMotor.Direction.REVERSE);
        motorFrontLeft.setDirection(DcMotor.Direction.REVERSE);

        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json";
        parameters.loggingEnabled      = true;
        parameters.loggingTag          = "IMU";

        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);

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

            angles   = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

            if (opMode == 0)
                travel(Math.atan2(gamepad1.left_stick_y, gamepad1.left_stick_x) - Math.PI / 4,
                        Math.sqrt(gamepad1.left_stick_x * gamepad1.left_stick_x +  gamepad1.left_stick_y * gamepad1.left_stick_y),
                        gamepad1.right_stick_x, doMaxSpeed);
            // Math.atan2(gamepad1.right_stick_y,gamepad1.right_stick_x) * 2
            else if (opMode == 1) {
                if (frontEnd)
                    relativeAngle = (Math.atan2(gamepad1.left_stick_y, gamepad1.left_stick_x) - Math.PI / 4) - angles.firstAngle;
                if (!frontEnd) {
                    relativeAngle = (Math.atan2(gamepad1.left_stick_y, gamepad1.left_stick_x) - Math.PI / 4) - (180 - angles.firstAngle);
                    if (relativeAngle < -180)
                        relativeAngle = 360 + relativeAngle;
                }
                if (Math.abs(relativeAngle) > 180) {
                    if (relativeAngle > 0)
                        relativeAngle = -(360 - Math.abs(relativeAngle));
                    else if (relativeAngle > 0)
                        relativeAngle = 360 - Math.abs(relativeAngle);

                    travel(relativeAngle, Math.sqrt(gamepad1.left_stick_x * gamepad1.left_stick_x +  gamepad1.left_stick_y * gamepad1.left_stick_y),
                            Math.atan2(gamepad1.right_stick_y,gamepad1.right_stick_x) * 2, doMaxSpeed);
                }
            }
            if (gamepad1.x && !xButtonNotDown) {
                xButtonNotDown = true;
                if (opMode == 0)
                    opMode = 1;
                else if (opMode == 1)
                    opMode = 0;
            }
            if (!gamepad1.x)
                xButtonNotDown = false;

            if (gamepad1.a && !aButtonNotDown) {
                aButtonNotDown = true;
                doMaxSpeed = !doMaxSpeed;
            }
            if (!gamepad1.a)
                aButtonNotDown = false;

            if (gamepad1.b && !bButtonNotDown) {
                bButtonNotDown = true;
                doAddDisplay = !doAddDisplay;
            }
            if (!gamepad1.b)
                bButtonNotDown = false;

            if (gamepad1.y && !yButtonNotDown) {
                yButtonNotDown = true;

                if (!frontEnd) {

                    motorBackLeft = hardwareMap.dcMotor.get("left_back");
                    motorBackRight = hardwareMap.dcMotor.get("right_back");
                    motorFrontLeft = hardwareMap.dcMotor.get("left_front");
                    motorFrontRight = hardwareMap.dcMotor.get("right_front");
                    frontEnd = true;

                }

                if (frontEnd) {

                    motorBackLeft = hardwareMap.dcMotor.get("right_front");
                    motorBackRight = hardwareMap.dcMotor.get("left_front");
                    motorFrontLeft = hardwareMap.dcMotor.get("right_back");
                    motorFrontRight = hardwareMap.dcMotor.get("left_back");
                    frontEnd = false;

                }
            }
            if (!gamepad1.y)
                yButtonNotDown = false;


                /*
                    motorBackLeft.setDirection(DcMotor.Direction.FORWARD);
                    motorFrontLeft.setDirection(DcMotor.Direction.FORWARD);
                    motorFrontRight.setDirection(DcMotor.Direction.REVERSE);
                    motorBackRight.setDirection(DcMotor.Direction.REVERSE);
                */
            if (opMode == 1)
                telemetry.addData("(x) Field Centric Active: ", true);
            else
                telemetry.addData("(x) Field Centric Active: ", false);

            telemetry.addData("(y) Robot Reversed: ", !frontEnd);
            telemetry.addData("(a) Speed Maximisation Active: ", doMaxSpeed);
            telemetry.addData("(b) Display Active: ", doAddDisplay);
            if (doAddDisplay) {
                telemetry.addData("Front Left Motor Power", motorFrontLeft.getPower());
                telemetry.addData("Front Right Motor Power", motorFrontRight.getPower());
                telemetry.addData("Back Left Motor Power", motorBackLeft.getPower());
                telemetry.addData("Back Right Motor Power", motorBackRight.getPower());
                telemetry.addData("Robot Angle (Yaw): ", angles.firstAngle);
                telemetry.addData("Relative Angle: ", relativeAngle);

            }

            telemetry.update();
        }

    }

    public void travel(double angle, double r, double tanRight, boolean speedMaxActive) {

        if (r > 1)
            r = 1;

        double rightX = tanRight;

        double v1 = r * Math.cos(angle) + rightX;
        double v2 = r * Math.sin(angle) - rightX;
        double v3 = r * Math.sin(angle) + rightX;
        double v4 = r * Math.cos(angle) - rightX;

        if (speedMaxActive) {
             v1 = r * Math.cos(angle) * (2 / Math.sqrt(2)) + rightX;
             v2 = r * Math.sin(angle) * (2 / Math.sqrt(2)) - rightX;
             v3 = r * Math.sin(angle) * (2 / Math.sqrt(2)) + rightX;
             v4 = r * Math.cos(angle) * (2 / Math.sqrt(2)) - rightX;
        }

        /*

        if (speedMaxActive) {
            if (Math.abs(v1) >= Math.abs(v2) && Math.abs(v1) >= Math.abs(v3) && Math.abs(v1) >= Math.abs(v4) && Math.abs(v1) < 1) {
                v1 = (1 / Math.abs(v1)) * v1;
                v2 = (1 / Math.abs(v1)) * v2;
                v3 = (1 / Math.abs(v1)) * v3;
                v4 = (1 / Math.abs(v1)) * v4;
            } else if (Math.abs(v2) >= Math.abs(v1) && Math.abs(v2) >= Math.abs(v3) && Math.abs(v2) >= Math.abs(v4) && Math.abs(v2) < 1) {
                v1 = (1 / Math.abs(v2)) * v1;
                v2 = (1 / Math.abs(v2)) * v2;
                v3 = (1 / Math.abs(v2)) * v3;
                v4 = (1 / Math.abs(v2)) * v4;
            } else if (Math.abs(v3) >= Math.abs(v1) && Math.abs(v3) >= Math.abs(v2) && Math.abs(v3) >= Math.abs(v4) && Math.abs(v3) < 1) {
                v1 = (1 / Math.abs(v3)) * v1;
                v2 = (1 / Math.abs(v3)) * v2;
                v3 = (1 / Math.abs(v3)) * v3;
                v4 = (1 / Math.abs(v3)) * v4;
            }  else if (Math.abs(v4) >= Math.abs(v1) && Math.abs(v4) >= Math.abs(v2) && Math.abs(v4) >= Math.abs(v3) && Math.abs(v4) < 1) {
                v1 = (1 / Math.abs(v4)) * v1;
                v2 = (1 / Math.abs(v4)) * v2;
                v3 = (1 / Math.abs(v4)) * v3;
                v4 = (1 / Math.abs(v4)) * v4;
            }
        }
        */


        motorFrontLeft.setPower(v1);
        motorFrontRight.setPower(v2);
        motorBackLeft.setPower(v3);
        motorBackRight.setPower(v4);

        /*
        telemetry.addData("FL Power",v1);
        telemetry.addData("FR Power",v2);
        telemetry.addData("BL Power",v3);
        telemetry.addData("BR Power",v4);
        telemetry.addData("r = ",r);
        telemetry.update();
        */

    }

}

