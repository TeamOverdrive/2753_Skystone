package org.firstinspires.ftc.teamcode.timsummerproject.teleop;

import android.content.Context;

import com.qualcomm.ftccommon.SoundPlayer;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.timsummerproject.util.nonStaticTelemetry;

@TeleOp(name = "TankDrive")
public class TankDrive extends LinearOpMode {

    DcMotor motorBackLeft;
    DcMotor motorBackRight;
    DcMotor motorFrontLeft;
    DcMotor motorFrontRight;

    private ElapsedTime runtime = new ElapsedTime();

    nonStaticTelemetry telemetry2 = new nonStaticTelemetry(telemetry);

    boolean soundPlaying = false;

    @Override
    public void runOpMode() throws InterruptedException {
/*
        motorBackLeft = hardwareMap.dcMotor.get("left1");
        motorBackRight = hardwareMap.dcMotor.get("right1");
        motorFrontLeft = hardwareMap.dcMotor.get("left2");
        motorFrontRight = hardwareMap.dcMotor.get("right2");

        motorBackRight.setDirection(DcMotor.Direction.REVERSE);
        motorFrontRight.setDirection(DcMotor.Direction.REVERSE);
*/
        Context myApp = hardwareMap.appContext;

        waitForStart();

        telemetry2.addPopUp("##########   ",
                            "#***ROBOT***#",
                            "#***ALERT***#",
                            "##########   ",  30, 1);

            while (opModeIsActive()) {
/*
                motorBackLeft.setPower(-gamepad1.left_stick_y);
                motorFrontLeft.setPower(-gamepad1.left_stick_y);
                motorBackRight.setPower(-gamepad1.right_stick_y);
                motorFrontRight.setPower(-gamepad1.right_stick_y);
*/
                if (this.runtime.seconds() > 0.1) {
                    if (telemetry2.side > 79)
                        telemetry2.side = 0;
                    else
                        telemetry2.side++;
                    runtime.reset();
                }
                telemetry2.setLine("                                                               ", 2);
                telemetry2.setLine("                                                               ",3);
                telemetry2.setLine("                                                               ",1);
                telemetry2.setLine("                                                               ",4);

                //telemetry2.setScrollLine(2,true);
                telemetry2.print();
                if (!(telemetry2.output[2] == null)) {
                    for (int i = 0; i < telemetry2.backupText.length; i++) {
                        telemetry.addLine(telemetry2.backupText[i]);
                    }
                }

            }

        }

    }
