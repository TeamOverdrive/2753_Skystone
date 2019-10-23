package org.firstinspires.ftc.teamcode.timsummerproject.teleop;

import android.content.Context;

import com.qualcomm.ftccommon.SoundPlayer;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name = "TankDrive")
public class TankDrive extends LinearOpMode {

    DcMotor motorBackLeft;
    DcMotor motorBackRight;
    DcMotor motorFrontLeft;
    DcMotor motorFrontRight;

    boolean soundPlaying = false;

    @Override
    public void runOpMode() throws InterruptedException {

        motorBackLeft = hardwareMap.dcMotor.get("left1");
        motorBackRight = hardwareMap.dcMotor.get("right1");
        motorFrontLeft = hardwareMap.dcMotor.get("left2");
        motorFrontRight = hardwareMap.dcMotor.get("right2");

        motorBackRight.setDirection(DcMotor.Direction.REVERSE);
        motorFrontRight.setDirection(DcMotor.Direction.REVERSE);

        Context myApp = hardwareMap.appContext;

            while (opModeIsActive()) {

                motorBackLeft.setPower(-gamepad1.left_stick_y);
                motorFrontLeft.setPower(-gamepad1.left_stick_y);
                motorBackRight.setPower(-gamepad1.right_stick_y);
                motorFrontRight.setPower(-gamepad1.right_stick_y);
                motorBackLeft.setPower(-gamepad1.left_stick_y);
                motorFrontLeft.setPower(-gamepad1.left_stick_y);
                motorBackRight.setPower(-gamepad1.right_stick_y);
                motorFrontRight.setPower(-gamepad1.right_stick_y);

            }

        }

    }
