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

@TeleOp(name = "Teleop", group = "Tests")
@Disabled

public class SkystoneTeleopTest extends LinearOpMode {

    Random rand = new Random();

    PodInfo driveTrain = new PodInfo();

    private float redColor = 1, blueColor = 1, greenColor = 1;
    private float RGB[] = {1,1,1};
    private int colorRand = 0;

    BNO055IMU imu;

    // State used for updating telemetry
    Orientation angles;
    Acceleration gravity;

    private DcMotor motorLeft;
    private DcMotor motorRight;
    private DcMotor motorFrontLeft;
    private DcMotor motorFrontRight;

    private double x = 2753, y = 2753, a = 2753, b = 2753;


    public void runOpMode() throws InterruptedException {

        motorLeft = hardwareMap.dcMotor.get("left_back");
        motorRight = hardwareMap.dcMotor.get("right_back");
        motorFrontLeft = hardwareMap.dcMotor.get("left_front");
        motorFrontRight = hardwareMap.dcMotor.get("right_front");

        motorRight.setDirection(DcMotor.Direction.REVERSE);
        motorFrontRight.setDirection(DcMotor.Direction.REVERSE);

        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json";
        parameters.loggingEnabled      = true;
        parameters.loggingTag          = "IMU";

        int relativeLayoutId = hardwareMap.appContext.getResources().getIdentifier("RelativeLayout", "id", hardwareMap.appContext.getPackageName());
        final View relativeLayout = ((Activity) hardwareMap.appContext).findViewById(relativeLayoutId);

        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);

        waitForStart();

        while (opModeIsActive()) {

            angles   = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

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

            if (gamepad1.dpad_down) {
                motorRight.setDirection(DcMotor.Direction.FORWARD);
                motorFrontRight.setDirection(DcMotor.Direction.FORWARD);
                motorLeft.setDirection(DcMotor.Direction.REVERSE);
                motorFrontLeft.setDirection(DcMotor.Direction.REVERSE);
            }
            if (gamepad1.dpad_up) {
                motorRight.setDirection(DcMotor.Direction.REVERSE);
                motorFrontRight.setDirection(DcMotor.Direction.REVERSE);
                motorLeft.setDirection(DcMotor.Direction.FORWARD);
                motorFrontLeft.setDirection(DcMotor.Direction.FORWARD);
            }

            if (gamepad1.y) {
                if (y == 2753) {
                    y = angles.firstAngle;
                } else {
                    turnTo(driveTrain, (float) y, angles.firstAngle);
                    motorFrontLeft.setPower(driveTrain.left_pod);
                    motorLeft.setPower(driveTrain.left_pod);
                    motorRight.setPower(driveTrain.right_pod);
                    motorFrontRight.setPower(driveTrain.right_pod);
                }
            } else if (gamepad1.x) {
                if (x == 2753) {
                    x = angles.firstAngle;
                } else {
                    turnTo(driveTrain, (float) x, angles.firstAngle);
                    motorFrontLeft.setPower(driveTrain.left_pod);
                    motorLeft.setPower(driveTrain.left_pod);
                    motorRight.setPower(driveTrain.right_pod);
                    motorFrontRight.setPower(driveTrain.right_pod);
                }
            } else if (gamepad1.a) {
                if (a == 2753) {
                    a = angles.firstAngle;
                } else {
                    turnTo(driveTrain, (float) a, angles.firstAngle);
                    motorFrontLeft.setPower(driveTrain.left_pod);
                    motorLeft.setPower(driveTrain.left_pod);
                    motorRight.setPower(driveTrain.right_pod);
                    motorFrontRight.setPower(driveTrain.right_pod);
                }
            } else if (gamepad1.b) {
                if (b == 2753) {
                    b = angles.firstAngle;
                } else {
                    turnTo(driveTrain, (float) b, angles.firstAngle);
                    motorFrontLeft.setPower(driveTrain.left_pod);
                    motorLeft.setPower(driveTrain.left_pod);
                    motorRight.setPower(driveTrain.right_pod);
                    motorFrontRight.setPower(driveTrain.right_pod);
                }
            }
            telemetry.addData("Angle: ",relativeAngle(getQuadrant(gamepad1.right_stick_x,
                    gamepad1.right_stick_y),gamepad1.right_stick_x,gamepad1.right_stick_y));

            if (gamepad1.right_bumper && gamepad1.left_stick_y > 0) {

                angleConversion(driveTrain, relativeAngle(getQuadrant(gamepad1.right_stick_x,
                        gamepad1.right_stick_y), gamepad1.right_stick_x, gamepad1.right_stick_y),
                        gamepad1.left_stick_y);

                motorFrontLeft.setPower(driveTrain.left_pod);
                motorLeft.setPower(driveTrain.left_pod);
                motorRight.setPower(driveTrain.right_pod);
                motorFrontRight.setPower(driveTrain.right_pod);

            } else if (gamepad1.right_bumper && gamepad1.left_stick_y < 0) {

                angleConversion(driveTrain, relativeAngle(getQuadrant(gamepad1.right_stick_x,
                        Math.abs(gamepad1.right_stick_y)), gamepad1.right_stick_x, gamepad1.right_stick_y),
                        -gamepad1.left_stick_y);

                motorFrontLeft.setPower(-driveTrain.left_pod);
                motorLeft.setPower(-driveTrain.left_pod);
                motorRight.setPower(-driveTrain.right_pod);
                motorFrontRight.setPower(-driveTrain.right_pod);

            } else if (gamepad1.left_bumper) {

                turnTo(driveTrain, (float) relativeAngle(getQuadrant(gamepad1.right_stick_x,
                        gamepad1.right_stick_y), gamepad1.right_stick_x, gamepad1.right_stick_y),
                        angles.firstAngle);

                if (Math.abs(gamepad1.left_stick_y) > 0) {
                    driveTrain.left_pod = -gamepad1.right_stick_y;
                    driveTrain.right_pod = -gamepad1.right_stick_y;
                }

                motorFrontLeft.setPower(driveTrain.left_pod);
                motorLeft.setPower(driveTrain.left_pod);
                motorRight.setPower(driveTrain.right_pod);
                motorFrontRight.setPower(driveTrain.right_pod);

            } else {
                if (Math.abs(gamepad1.right_stick_y) > 0 || Math.abs(gamepad1.left_stick_y) > 0) {
                    driveTrain.right_pod = -gamepad1.right_stick_y;
                    driveTrain.left_pod = -gamepad1.left_stick_y;
                }

                motorFrontLeft.setPower(driveTrain.left_pod);
                motorLeft.setPower(driveTrain.left_pod);
                motorRight.setPower(driveTrain.right_pod);
                motorFrontRight.setPower(driveTrain.right_pod);

            }
            telemetry.update();
            if (gamepad1.atRest()) {
                motorFrontLeft.setPower(0);
                motorLeft.setPower(0);
                motorRight.setPower(0);
                motorFrontRight.setPower(0);

            }

        }

        idle();
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
        telemetry.addData("Left Power: ", drive.left_pod);
        telemetry.addData("Right Power: ", drive.right_pod);

    }
    public void turnTo(PodInfo driveTrain, float angle, float gyroAngle) {
        //float power = Math.abs(Math.abs(angle) - Math.abs(gyroAngle))/90;
        float power = 1;
        if (angle > 0) {
            if (gyroAngle > angle - 180 && gyroAngle <= angle) {
                // Turn Right
                driveTrain.left_pod = -power;
                driveTrain.right_pod = power;
            } else {
                // Turn Left
                driveTrain.left_pod = power;
                driveTrain.right_pod = -power;
            }
        } else if (angle < 0) {
            if (gyroAngle < angle + 180 && gyroAngle >= angle) {
                // Turn Left
                driveTrain.left_pod = power;
                driveTrain.right_pod = -power;
            } else {
                // Turn Right
                driveTrain.left_pod = -power;
                driveTrain.right_pod = power;
            }
        }
      /*  if (gyroAngle == angle) {
            driveTrain.left_pod = 0;
            driveTrain.right_pod = 0;
        }
        */
    }
}

