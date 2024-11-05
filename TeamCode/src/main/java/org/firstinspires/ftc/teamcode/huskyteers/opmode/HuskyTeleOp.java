package org.firstinspires.ftc.teamcode.huskyteers.opmode;

import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;


import org.firstinspires.ftc.teamcode.huskyteers.HuskyBot;
import org.firstinspires.ftc.teamcode.huskyteers.utils.GamepadUtils;

import java.util.concurrent.atomic.AtomicBoolean;

@TeleOp
public class HuskyTeleOp extends HuskyBot {
    private DcMotor LS;
    @Override
    public void runOpMode() {
        instantiateMotors(new Pose2d(0, 0, 0));
        initVisionPortal();
        LS = hardwareMap.get(DcMotor.class, "LS");
        LS.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);  // Ensures it holds position when power is zero
        LS.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        waitForStart();
        if (isStopRequested()) return;
        GamepadUtils gamepad1Utils = new GamepadUtils();
        GamepadUtils gamepad2Utils = new GamepadUtils();
        gamepad1Utils.addRisingEdge("start", (pressed) -> drive.pose = new Pose2d(this.drive.pose.position, 0));

        AtomicBoolean usingFieldCentric = new AtomicBoolean(false);

        gamepad1Utils.addRisingEdge("a", (pressed) -> {
            usingFieldCentric.set(!usingFieldCentric.get());
            gamepad1.rumble(200);
        });
        gamepad1Utils.addRisingEdge("dpad_up", (pressed) -> {
            visionPortal.resumeStreaming();
        });

        while (opModeIsActive() && !isStopRequested()) {
            gamepad1Utils.processUpdates(gamepad1);
            gamepad2Utils.processUpdates(gamepad2);

            double speed = (0.35 + 0.5 * gamepad1.left_trigger);
            if (usingFieldCentric.get()) {
                telemetry.addData("Drive Mode", "Field Centric");
                fieldCentricDriveRobot(gamepad1.left_stick_y, -gamepad1.left_stick_x, -gamepad1.right_stick_x, speed);
            } else {
                telemetry.addData("Drive Mode", "Tank");
                driveRobot(gamepad1.left_stick_y, -gamepad1.left_stick_x, -gamepad1.right_stick_x, speed);
            }
            double lsPower;
            if (gamepad2.left_stick_y > 0.1) {
                lsPower = 0.1;  // Move in one direction
            } else if (gamepad2.left_stick_y < -0.1) {
                lsPower = -0.1;  // Move in the opposite direction
            } else {
                lsPower = 0;  // Stop the motor when stick is centered
            }
            LS.setPower(lsPower);

            telemetry.addData("LS Encoder Position", LS.getCurrentPosition());
            telemetry.update();
            sleep(20);
        }
        visionPortal.close();

    }
}
