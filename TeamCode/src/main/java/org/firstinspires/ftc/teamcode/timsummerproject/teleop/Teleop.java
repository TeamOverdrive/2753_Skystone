package org.firstinspires.ftc.teamcode.timsummerproject.teleop;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.timsummerproject.subsystems.DriveTrain;


@TeleOp(name = "Teleop", group = "TeleOp")
public class Teleop extends LinearOpMode {

    private DcMotor motorBackLeft;
    private DcMotor motorBackRight;
    private DcMotor motorFrontLeft;
    private DcMotor motorFrontRight;

    DriveTrain drive = new DriveTrain();

    @Override
    public void runOpMode() throws InterruptedException {

        BNO055IMU imu;
        Orientation angles;

        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json";
        parameters.loggingEnabled      = true;
        parameters.loggingTag          = "IMU";

        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);

        waitForStart();

        while(opModeIsActive()) {

            initMotors();
            angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            if (gamepad1.right_trigger > 0)
                if (gamepad1.left_stick_y > 0) {
                    customMove(-gamepad1.right_trigger, false);
                }
                else
                customMove(gamepad1.right_trigger, true);
            else if (gamepad1.left_trigger > 0)
                if (gamepad1.left_stick_y > 0) {
                    customMove(-gamepad1.left_trigger, true);
                }
                else
                customMove(gamepad1.left_trigger, false);
            else if (gamepad1.left_bumper && (Math.sqrt(gamepad1.left_stick_x * gamepad1.left_stick_x +  gamepad1.left_stick_y * gamepad1.left_stick_y) > 0.2)){

                while (gamepad1.right_bumper) {
                    setBrake();
                    stopMove();
                    removeBrake();
                }
                teleDriveSMART(angles);
                update();
            }
            else {
                while (gamepad1.right_bumper) {
                    setBrake();
                    stopMove();
                    removeBrake();
                }

                teleDrive(angles);
                update();
            }


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

        drive.move(relativeAngle, Math.sqrt(gamepad1.left_stick_x * gamepad1.left_stick_x +  gamepad1.left_stick_y * gamepad1.left_stick_y),
                gamepad1.right_stick_x);
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
            drive.move(relativeAngle, Math.sqrt(gamepad1.left_stick_x * gamepad1.left_stick_x +  gamepad1.left_stick_y * gamepad1.left_stick_y),
                    0.6);
        }
        else if (-relativeAngle +  Math.PI/4 < 0) {
            drive.move(relativeAngle, Math.sqrt(gamepad1.left_stick_x * gamepad1.left_stick_x +  gamepad1.left_stick_y * gamepad1.left_stick_y),
                    -0.6);
        } else {
            drive.move(relativeAngle, Math.sqrt(gamepad1.left_stick_x * gamepad1.left_stick_x +  gamepad1.left_stick_y * gamepad1.left_stick_y),
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

}



