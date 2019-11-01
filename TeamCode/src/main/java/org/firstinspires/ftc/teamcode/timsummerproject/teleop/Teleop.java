package org.firstinspires.ftc.teamcode.timsummerproject.teleop;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.timsummerproject.subsystems.DriveTrain;
import org.firstinspires.ftc.teamcode.timsummerproject.util.nonStaticTelemetry;


@TeleOp(name = "Teleop1.x", group = "TeleOp")
public class    Teleop extends LinearOpMode {

    private DcMotor motorBackLeft;
    private DcMotor motorBackRight;
    private DcMotor motorFrontLeft;
    private DcMotor motorFrontRight;

    String currentDriveMode = "Field-Centric";

    private float aPos = 2753, bPos = 2753, xPos = 2753, yPos = 2753;
    DriveTrain drive = new DriveTrain();

    int lnSelect = 7;
    float rotateSpeed = 1, speedReduction = 1;
    boolean autoBrake, wasdpadDown = false, wasdpadUp = false;

    private ElapsedTime runtime = new ElapsedTime();

    nonStaticTelemetry telemetry2 = new nonStaticTelemetry(telemetry);

    @Override
    public void runOpMode() throws InterruptedException {

        BNO055IMU imu;
        Orientation angles;

        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json";
        parameters.loggingEnabled = true;
        parameters.loggingTag = "IMU";

        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);

        waitForStart();

        telemetry2.setLine("__________________________________________", 1);
        telemetry2.setLine("               ", 6);

        while (opModeIsActive()) {

            initMotors();
            angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            if (gamepad1.right_trigger > 0) {
                currentDriveMode = "Drag Turning";
                if (gamepad1.left_stick_y > 0) {
                    customMove(-gamepad1.right_trigger, false);
                } else
                    customMove(gamepad1.right_trigger, true);
            } else if (gamepad1.left_trigger > 0) {
                currentDriveMode = "Drag Turning";
                if (gamepad1.left_stick_y > 0) {
                    customMove(-gamepad1.left_trigger, true);
                } else
                    customMove(gamepad1.left_trigger, false);
            } else if (gamepad1.left_bumper && (Math.sqrt(gamepad1.left_stick_x * gamepad1.left_stick_x + gamepad1.left_stick_y * gamepad1.left_stick_y) > 0.2)) {

                currentDriveMode = "Smart Mode";
                while (gamepad1.right_bumper) {
                    currentDriveMode = "BRAKE";
                    setBrake();
                    stopMove();
                    removeBrake();
                }
                teleDriveSMART(angles);
                update();
            } else if (gamepad1.a) {
                currentDriveMode = "Target-A";
                if (aPos == 2753) {
                    aPos = angles.firstAngle - 180;
                    if (aPos < -180)
                        aPos = -aPos;
                } else {
                    teleDrive(angles, aPos);
                    update();
                }
            } else if (gamepad1.b) {
                currentDriveMode = "Target-B";
                if (bPos == 2753) {
                    bPos = angles.firstAngle - 180;
                    if (bPos < -180)
                        bPos = -bPos;
                } else {
                    teleDrive(angles, bPos);
                    update();
                }
            } else if (gamepad1.x) {
                currentDriveMode = "Target-X";
                if (xPos == 2753) {
                    xPos = angles.firstAngle - 180;
                    if (xPos < -180)
                        xPos = -xPos;
                } else {
                    teleDrive(angles, xPos);
                    update();
                }
            }
            else if (gamepad1.y) {
                currentDriveMode = "Target-Y";
                if (yPos == 2753) {
                    yPos = angles.firstAngle - 180;
                    if (yPos < -180)
                        yPos = -yPos;
                } else {
                    teleDrive(angles, yPos);
                    update();
                }
            }
            else {
                currentDriveMode = "Field-Centric";
                while (gamepad1.right_bumper) {
                    currentDriveMode = "BRAKE";
                    setBrake();
                    stopMove();
                    removeBrake();
                }

                teleDrive(angles);
                update();

            }
            if (autoBrake) {
                setBrake();
            } else {
                removeBrake();
            }
            if (this.runtime.seconds() > 0.1) {
                if (telemetry2.side > 50)
                    telemetry2.side = 0;
                else
                    telemetry2.side++;
                runtime.reset();
            }
            if (gamepad1.dpad_up) {
                if (!wasdpadUp) {
                    lnSelect--;
                    wasdpadUp = true;
                }
            }
            if (!gamepad1.dpad_up) {
                wasdpadUp = false;
            }
            if (gamepad1.dpad_down) {
                if (!wasdpadDown) {
                    lnSelect++;
                    wasdpadDown = true;
                }
            }
            if (!gamepad1.dpad_down) {
                wasdpadDown = false;
            }

            if (gamepad1.dpad_right) {
                if (lnSelect == 7) {
                    speedReduction = (float) 0.6;
                } else if (lnSelect == 8) {
                    drive.speedMax = (float) (2 / Math.sqrt(2));
                } else if (lnSelect == 9) {
                    rotateSpeed = (float) 0.6;
                } else if (lnSelect == 10) {
                    autoBrake = true;
                }
            }
            if (gamepad1.dpad_left) {
                if (lnSelect == 7) {
                    speedReduction = 1;
                } else if (lnSelect == 8) {
                    drive.speedMax = 1;
                } else if (lnSelect == 9) {
                    rotateSpeed = 1;
                } else if (lnSelect == 10) {
                    autoBrake = false;
                }
            }
            if (lnSelect == 6)
                lnSelect = 7;
            if (lnSelect == 11)
                lnSelect = 10;

            if (!(telemetry2.output[2] == null)) {
                for (int i = 0; i < telemetry2.backupText.length; i++) {
                    telemetry.addLine(telemetry2.backupText[i]);
                }
            }
            telemetry2.setLine("Drive Train Mode: " + currentDriveMode + "    Gamepad1 Active: " + !gamepad1.atRest() +
                    "   |                                                                               ", 0);
            telemetry2.setLine("      Pwr-LB: " + drive.BackLeft + " Pwr-RB: " + drive.BackRight + " Pwr-LF: " + drive.FrontLeft
                    + " Pwr-RF: " + drive.FrontRight, 2);
            telemetry2.setScrollLine(2, false);
            telemetry2.setLine("      1st-angle: " + angles.firstAngle + " 2nd-angle: " + angles.secondAngle +
                    " 3rd-angle: " + angles.thirdAngle, 3);
            telemetry2.setScrollLine(3, false);
            telemetry2.setLine("      aPos: " + aPos + " bPos: " + bPos +
                    " xPos: " + xPos + " yPos: " + yPos, 4);
            telemetry2.setScrollLine(4, false);
            if (Math.abs(angles.secondAngle) > 8 || Math.abs(angles.thirdAngle) > 8)
                telemetry2.addPopUp("##########     ",
                        "#****Robot****#",
                        "#**Tipping!***#",
                        "##########     ", 0, 2);
            else
                telemetry2.addPopUp("               ",
                        "               ",
                        "               ",
                        "               ", 0, 2);
            telemetry2.setLine("________________________________________", 5);
            if (speedReduction == 1)
                telemetry2.setLine("   Speed Reduction: INACTIVE", 7);
            else
                telemetry2.setLine("   Speed Reduction: ACTIVE", 7);
            if (drive.speedMax > 1)
                telemetry2.setLine("   Speed Max: ACTIVE", 8);
            else
                telemetry2.setLine("   Speed Max: INACTIVE", 8);
            if (rotateSpeed == 1)
                telemetry2.setLine("   Slowed Turning: INACTIVE", 9);
            else
                telemetry2.setLine("   Slowed Turning: ACTIVE", 9);
            if (autoBrake)
                telemetry2.setLine("   Auto Braking: ACTIVE", 10);
            else
                telemetry2.setLine("   Auto Braking: INACTIVE", 10);

            telemetry2.override[lnSelect - 1][1] = ' ';
            telemetry2.override[lnSelect + 1][1] = ' ';
            telemetry2.override[lnSelect][1] = '>';
            telemetry2.print();
        }

    }
    public void initMotors() {

        motorBackLeft = hardwareMap.get(DcMotor.class, "left_back");
        motorBackRight = hardwareMap.get(DcMotor.class, "right_back");
        motorFrontLeft = hardwareMap.get(DcMotor.class, "left_front");
        motorFrontRight = hardwareMap.get(DcMotor.class, "right_front");

        motorBackRight.setDirection(DcMotor.Direction.REVERSE);
        motorFrontRight.setDirection(DcMotor.Direction.REVERSE);

    }
    public void teleDrive(Orientation angles) {
        double relativeAngle;
        relativeAngle = (Math.atan2(-gamepad1.left_stick_y, gamepad1.left_stick_x) - Math.PI / 4) - Math.toRadians(angles.firstAngle);
        if (Math.abs(relativeAngle) > Math.PI) {
            if (relativeAngle > 0)
                relativeAngle = -(Math.PI * 2 - Math.abs(relativeAngle));
            else if (relativeAngle > 0)
                relativeAngle = Math.PI * 2 - Math.abs(relativeAngle);
        }

        drive.move(relativeAngle, Math.sqrt(gamepad1.left_stick_x * gamepad1.left_stick_x +  gamepad1.left_stick_y * gamepad1.left_stick_y) * speedReduction,
                gamepad1.right_stick_x * rotateSpeed);
    }
    public void teleDriveSMART(Orientation angles) {
        double relativeAngle;
        relativeAngle = (Math.atan2(-gamepad1.left_stick_y, gamepad1.left_stick_x) - Math.PI / 4) - Math.toRadians(angles.firstAngle);
        if (Math.abs(relativeAngle) > Math.PI) {
            if (relativeAngle > 0)
                relativeAngle = -(Math.PI * 2 - Math.abs(relativeAngle));
            else if (relativeAngle < 0)
                relativeAngle = Math.PI * 2 - Math.abs(relativeAngle);
        }
        if (-relativeAngle + Math.PI/4> 0) {
            drive.move(relativeAngle, Math.sqrt(gamepad1.left_stick_x * gamepad1.left_stick_x +  gamepad1.left_stick_y * gamepad1.left_stick_y)* speedReduction,
                    1.2);
        }
        else if (-relativeAngle +  Math.PI/4 < 0) {
            drive.move(relativeAngle, Math.sqrt(gamepad1.left_stick_x * gamepad1.left_stick_x +  gamepad1.left_stick_y * gamepad1.left_stick_y)* speedReduction,
                    -1.2);
        } else {
            drive.move(relativeAngle, Math.sqrt(gamepad1.left_stick_x * gamepad1.left_stick_x +  gamepad1.left_stick_y * gamepad1.left_stick_y)* speedReduction,
                    0);
        }
    }

    public void update() {
        motorFrontLeft.setPower(drive.FrontLeft);
        motorFrontRight.setPower(drive.FrontRight);
        motorBackLeft.setPower(drive.BackLeft);
        motorBackRight.setPower(drive.BackRight);

    }
    public void setBrake() {

        motorFrontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorBackRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorFrontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorBackLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }
    public void removeBrake() {

        motorFrontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        motorBackRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        motorFrontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        motorBackLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

    }
    public void stopMove() {

        motorFrontLeft.setPower(0);
        motorFrontRight.setPower(0);
        motorBackLeft.setPower(0);
        motorBackRight.setPower(0);

    }
    public void customMove(float speed, boolean goLeft) {
        if (!goLeft) {
            motorFrontLeft.setPower((1 - Math.sqrt(Math.abs(speed))) * (speed/Math.abs(speed)));
            motorFrontRight.setPower(speed);
            motorBackLeft.setPower((1 - Math.sqrt(Math.abs(speed))) * (speed/Math.abs(speed)));
            motorBackRight.setPower(speed);
        } else {
            motorFrontLeft.setPower(speed);
            motorFrontRight.setPower((1 - Math.sqrt(Math.abs(speed))) * (speed/Math.abs(speed)));
            motorBackLeft.setPower(speed);
            motorBackRight.setPower((1 - Math.sqrt(Math.abs(speed))) * (speed/Math.abs(speed)));
        }
    }
    public void teleDrive(Orientation angles, float target) {
        double relativeAngle;
        relativeAngle = (Math.atan2(-gamepad1.left_stick_y, gamepad1.left_stick_x) - Math.PI/4) - Math.toRadians(angles.firstAngle)- Math.PI / 4;
        if (Math.abs(relativeAngle) > Math.PI) {
            if (relativeAngle > 0)
                relativeAngle = -(Math.PI * 2 - Math.abs(relativeAngle));
            else if (relativeAngle < 0)
                relativeAngle = Math.PI * 2 - Math.abs(relativeAngle);
        }
        double relativeTarget = Math.toRadians(target) - Math.toRadians(angles.firstAngle);
        if (Math.abs(relativeTarget) > Math.PI) {
            if (relativeTarget > 0)
                relativeTarget = -(Math.PI * 2 - Math.abs(relativeTarget));
            else if (relativeTarget < 0)
                relativeTarget = Math.PI * 2 - Math.abs(relativeTarget);
        }

        if (relativeTarget > 0) {
            drive.move(relativeAngle, Math.sqrt(gamepad1.left_stick_x * gamepad1.left_stick_x +  gamepad1.left_stick_y * gamepad1.left_stick_y)* speedReduction,
                    (Math.abs(relativeAngle/Math.PI)));
        } else {
            drive.move(relativeAngle, Math.sqrt(gamepad1.left_stick_x * gamepad1.left_stick_x +  gamepad1.left_stick_y * gamepad1.left_stick_y)* speedReduction,
                    - (Math.abs(relativeAngle/Math.PI)));

        }

    }

}



