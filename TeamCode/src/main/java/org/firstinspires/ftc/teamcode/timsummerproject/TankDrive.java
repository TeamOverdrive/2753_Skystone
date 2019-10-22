package org.firstinspires.ftc.teamcode.timsummerproject;

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

<<<<<<< HEAD
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

=======
    String  sounds[] =  {"ss_alarm", "ss_bb8_down", "ss_bb8_up", "ss_darth_vader", "ss_fly_by",
            "ss_mf_fail", "ss_laser", "ss_laser_burst", "ss_light_saber", "ss_light_saber_long", "ss_light_saber_short",
            "ss_light_speed", "ss_mine", "ss_power_up", "ss_r2d2_up", "ss_roger_roger", "ss_siren", "ss_wookie" };
    boolean soundPlaying = false;

    public void runOpMode() throws InterruptedException {

        int     soundIndex      = 0;
        int     soundID         = -1;
        boolean was_dpad_up     = false;
        boolean was_dpad_down   = false;

        Context myApp = hardwareMap.appContext;

        // create a sound parameter that holds the desired player parameters.
>>>>>>> master
        SoundPlayer.PlaySoundParams params = new SoundPlayer.PlaySoundParams();
        params.loopControl = 0;
        params.waitForNonLoopingSoundsToFinish = true;

<<<<<<< HEAD
        int     soundID         = -1;
=======
        motorBackLeft = hardwareMap.dcMotor.get("left1");
        motorBackRight = hardwareMap.dcMotor.get("right1");
        motorFrontLeft = hardwareMap.dcMotor.get("left2");
        motorFrontRight = hardwareMap.dcMotor.get("right2");

        motorBackRight.setDirection(DcMotor.Direction.REVERSE);
        motorFrontRight.setDirection(DcMotor.Direction.REVERSE);
>>>>>>> master

        waitForStart();

        while(opModeIsActive()) {

<<<<<<< HEAD
            if (gamepad1.right_bumper && !soundPlaying) {

                // Determine Resource IDs for the sounds you want to play, and make sure it's valid.
                if ((soundID = myApp.getResources().getIdentifier("ss_wookie" , "raw", myApp.getPackageName())) != 0){

                    // Signal that the sound is now playing.
                    soundPlaying = true;

                    // Start playing, and also Create a callback that will clear the playing flag when the sound is complete.
                    SoundPlayer.getInstance().startPlaying(myApp, soundID, params, null,
                            new Runnable() {
                                public void run() {
                                    soundPlaying = false;
                                }} );
                }
            }

            motorBackLeft.setPower(-gamepad1.left_stick_y);
            motorFrontLeft.setPower(-gamepad1.left_stick_y);
            motorBackRight.setPower(-gamepad1.right_stick_y);
            motorFrontRight.setPower(-gamepad1.right_stick_y);
=======
            motorBackLeft.setPower(-gamepad1.left_stick_y);
            motorFrontLeft.setPower(-gamepad1.left_stick_y);
            motorBackRight.setPower(-gamepad1.right_stick_y);
            motorFrontRight.setPower(-gamepad1.right_stick_y);
            if (!isStopRequested()) {
                if (gamepad1.dpad_down && !was_dpad_down) {
                    // Go to next sound (with list wrap) and display it
                    soundIndex = (soundIndex + 1) % sounds.length;
                }

                if (gamepad1.dpad_up && !was_dpad_up) {
                    // Go to previous sound (with list wrap) and display it
                    soundIndex = (soundIndex + sounds.length - 1) % sounds.length;
                }

                // Look for trigger to see if we should play sound
                // Only start a new sound if we are currently not playing one.
                if (gamepad1.right_bumper && !soundPlaying) {

                    // Determine Resource IDs for the sounds you want to play, and make sure it's valid.
                    if ((soundID = myApp.getResources().getIdentifier(sounds[soundIndex], "raw", myApp.getPackageName())) != 0) {

                        // Signal that the sound is now playing.
                        soundPlaying = true;

                        // Start playing, and also Create a callback that will clear the playing flag when the sound is complete.
                        SoundPlayer.getInstance().startPlaying(myApp, soundID, params, null,
                                new Runnable() {
                                    public void run() {
                                        soundPlaying = false;
                                    }
                                });
                    }
                }

                // Remember the last state of the dpad to detect changes.
                was_dpad_up = gamepad1.dpad_up;
                was_dpad_down = gamepad1.dpad_down;

                // Display the current sound choice, and the playing status.
                telemetry.addData("", "Use DPAD up/down to choose sound.");
                telemetry.addData("", "Press Right Bumper to play sound.");
                telemetry.addData("", "");
                telemetry.addData("Sound >", sounds[soundIndex]);
                telemetry.addData("Status >", soundPlaying ? "Playing" : "Stopped");
                telemetry.update();
            }
>>>>>>> master
        }

    }


}
