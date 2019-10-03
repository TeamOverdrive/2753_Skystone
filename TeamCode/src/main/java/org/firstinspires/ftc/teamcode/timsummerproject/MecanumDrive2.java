package org.firstinspires.ftc.teamcode.timsummerproject;

import android.app.Activity;
import android.view.View;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.hardware.bosch.BNO055IMU;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

@TeleOp(name = "MecDrive2.0", group = "TeleOp")
public class MecanumDrive2 extends LinearOpMode {

    boolean xButtonNotDown = false, aButtonNotDown = false, bButtonNotDown = false;
    boolean doMaxSpeed = false,doAddDisplay = false;

    double relativeAngle, turnToAngle;

    DcMotor motorBackLeft;
    DcMotor motorBackRight;
    DcMotor motorFrontLeft;
    DcMotor motorFrontRight;

    int opMode = 2;

    @Override
    public void runOpMode() throws InterruptedException {

        BNO055IMU imu;
        Orientation angles;

        DriveTrain drivetrain = new DriveTrain();

        motorBackLeft = hardwareMap.dcMotor.get("left_back");
        motorBackRight = hardwareMap.dcMotor.get("right_back");
        motorFrontLeft = hardwareMap.dcMotor.get("left_front");
        motorFrontRight = hardwareMap.dcMotor.get("right_front");

        motorBackRight.setDirection(DcMotor.Direction.REVERSE);
        motorFrontRight.setDirection(DcMotor.Direction.REVERSE);

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

            angles   = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

            if (opMode == 1)
                drivetrain.travel(Math.atan2(-gamepad1.left_stick_y, gamepad1.left_stick_x) - Math.PI / 4,
                        Math.sqrt(gamepad1.left_stick_x * gamepad1.left_stick_x +  gamepad1.left_stick_y * gamepad1.left_stick_y),
                        gamepad1.right_stick_x, doMaxSpeed);
            else if (opMode == 2 && gamepad1.left_trigger > 0 || gamepad1.right_trigger > 0) {
                relativeAngle = (Math.atan2(-gamepad1.left_stick_y, gamepad1.left_stick_x) - Math.PI / 4) - Math.toRadians(angles.firstAngle);
                if (Math.abs(relativeAngle) > Math.PI) {
                    if (relativeAngle > 0)
                        relativeAngle = -(Math.PI * 2 - Math.abs(relativeAngle));
                    else if (relativeAngle > 0)
                        relativeAngle = Math.PI * 2 - Math.abs(relativeAngle);
                }

                drivetrain.travel(relativeAngle, Math.sqrt(gamepad1.left_stick_x * gamepad1.left_stick_x + gamepad1.left_stick_y * gamepad1.left_stick_y),
                        gamepad1.right_trigger - gamepad1.left_trigger, doMaxSpeed);

            } else {
                relativeAngle = (Math.atan2(-gamepad1.left_stick_y, gamepad1.left_stick_x) - Math.PI / 4) - Math.toRadians(angles.firstAngle);
                if (Math.abs(relativeAngle) > Math.PI) {
                    if (relativeAngle > 0)
                        relativeAngle = -(Math.PI * 2 - Math.abs(relativeAngle));
                    else if (relativeAngle > 0)
                        relativeAngle = Math.PI * 2 - Math.abs(relativeAngle);
                }

                drivetrain.travel(relativeAngle, Math.sqrt(gamepad1.left_stick_x * gamepad1.left_stick_x +  gamepad1.left_stick_y * gamepad1.left_stick_y),
                        gamepad1.right_stick_x, doMaxSpeed);

            }

            /*
            if (gamepad1.x && !xButtonNotDown) {
                xButtonNotDown = true;
                opMode = (opMode + 1);
                if (opMode == 4
                    opMode = 1;
            } else if (!gamepad1.x)
                xButtonNotDown = false;
            */

            if (gamepad1.a && !aButtonNotDown) {
                aButtonNotDown = true;
                doMaxSpeed = !doMaxSpeed;
            } else if (!gamepad1.a)
                aButtonNotDown = false;

            if (gamepad1.b && !bButtonNotDown) {
                bButtonNotDown = true;
                doAddDisplay = !doAddDisplay;
            } else if (!gamepad1.b)
                bButtonNotDown = false;

            motorFrontLeft.setPower(drivetrain.v1);
            motorFrontRight.setPower(drivetrain.v2);
            motorBackLeft.setPower(drivetrain.v3);
            motorBackRight.setPower(drivetrain.v4);

        }

    }

}

