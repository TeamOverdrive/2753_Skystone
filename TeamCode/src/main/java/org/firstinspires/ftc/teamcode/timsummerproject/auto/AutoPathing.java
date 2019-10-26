package org.firstinspires.ftc.teamcode.timsummerproject.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.timsummerproject.subsystems.DriveTrain;

@Autonomous(name="Auto Move", group="Pushbot")
public class AutoPathing extends LinearOpMode {

    DriveTrain drive = new DriveTrain();

    private ElapsedTime runtime = new ElapsedTime();

    DcMotor motorBackLeft;
    DcMotor motorBackRight;
    DcMotor motorFrontLeft;
    DcMotor motorFrontRight;
    double xPos;
    double yPos;
    double zPos;

    public void runOpMode() {

        motorBackLeft = hardwareMap.dcMotor.get("left_back");
        motorBackRight = hardwareMap.dcMotor.get("right_back");
        motorFrontLeft = hardwareMap.dcMotor.get("left_front");
        motorFrontRight = hardwareMap.dcMotor.get("right_front");

        motorBackRight.setDirection(DcMotor.Direction.REVERSE);
        motorFrontRight.setDirection(DcMotor.Direction.REVERSE);


        waitForStart();




    }

    public void initMotors() {

        motorBackLeft = hardwareMap.get(DcMotor.class, "left_back");
        motorBackRight = hardwareMap.get(DcMotor.class, "right_back");
        motorFrontLeft = hardwareMap.get(DcMotor.class, "left_front");
        motorFrontRight = hardwareMap.get(DcMotor.class, "right_front");

        motorBackRight.setDirection(DcMotor.Direction.REVERSE);
        motorFrontRight.setDirection(DcMotor.Direction.REVERSE);

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

        motorFrontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
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
    public void moveInch(int inches, float speed, float timeout) {

        int motorFrontRightTP = motorFrontRight.getCurrentPosition() + (int)(inches * drive.COUNTS_PER_INCH);
        int motorBackRightTP = motorBackRight.getCurrentPosition() + (int)(inches * drive.COUNTS_PER_INCH);
        int motorFrontLeftTP = motorFrontLeft.getCurrentPosition() + (int)(inches * drive.COUNTS_PER_INCH);
        int motorBackLeftTP = motorBackLeft.getCurrentPosition() + (int)(inches * drive.COUNTS_PER_INCH);

        motorFrontRight.setTargetPosition(motorFrontRightTP);
        motorBackRight.setTargetPosition(motorBackRightTP);
        motorFrontLeft.setTargetPosition(motorFrontLeftTP);
        motorBackLeft.setTargetPosition(motorBackLeftTP);

        motorFrontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorBackRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorFrontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorBackLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        this.runtime.reset();

        motorFrontRight.setPower(Math.abs(speed));
        motorBackRight.setPower(Math.abs(speed));
        motorFrontLeft.setPower(Math.abs(speed));
        motorBackLeft.setPower(Math.abs(speed));

        while (opModeIsActive() &&
                (this.runtime.seconds() < timeout) &&
                (motorFrontRight.isBusy() && motorBackRight.isBusy() && motorFrontLeft.isBusy() && motorBackLeft.isBusy())) {

        }

        motorFrontRight.setPower(0);
        motorBackRight.setPower(0);
        motorFrontLeft.setPower(0);
        motorBackLeft.setPower(0);

        motorFrontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorBackRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorFrontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorBackLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        xPos = ConceptVuforiaSkyStoneNavigation.xPosition;
        yPos = ConceptVuforiaSkyStoneNavigation.yPosition;
        zPos = ConceptVuforiaSkyStoneNavigation.zPosition;

    }

}
