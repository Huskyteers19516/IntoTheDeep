package org.firstinspires.ftc.teamcode.huskyteers.opmode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.huskyteers.HuskyBot;
import org.firstinspires.ftc.teamcode.huskyteers.hardware.ArmSlide;
import org.firstinspires.ftc.teamcode.huskyteers.utils.GamepadUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@TeleOp
public class HuskyTeleOp extends HuskyBot {
    final private FtcDashboard dash = FtcDashboard.getInstance();
    private List<Action> runningActions = new ArrayList<>();

    @Override
    public void runOpMode() {
        instantiateMotors(new Pose2d(0, 0, 0));
        initVisionPortal();

        waitForStart();
        if (isStopRequested())
            return;
        GamepadUtils gamepad1Utils = new GamepadUtils();
        GamepadUtils gamepad2Utils = new GamepadUtils();
        gamepad1Utils.addRisingEdge("start", (pressed) -> drive.pose = new Pose2d(this.drive.pose.position, 0));

        AtomicBoolean usingFieldCentric = new AtomicBoolean(false);

        gamepad1Utils.addRisingEdge("right_bumper", (pressed) -> armSlide.setPosition(ArmSlide.EXTEND_POSITION));
        gamepad1Utils.addRisingEdge("left_bumper", (pressed) -> armSlide.setPosition(ArmSlide.RETRACT_POSITION));

        gamepad1Utils.addRisingEdge("a", (pressed) -> {
            usingFieldCentric.set(!usingFieldCentric.get());
            gamepad1.rumble(200);
        });
        gamepad1Utils.addRisingEdge("dpad_up", (pressed) -> {
            visionPortal.resumeStreaming();
        });

        while (opModeIsActive() && !isStopRequested()) {
            TelemetryPacket packet = new TelemetryPacket();

            gamepad1Utils.processUpdates(gamepad1);
            gamepad2Utils.processUpdates(gamepad2);

            double speed = (0.35 + 0.5 * gamepad1.left_trigger);
            if (gamepad1.a) {
                usingFieldCentric.set(!usingFieldCentric.get());
            }
            if (usingFieldCentric.get()) {
                telemetry.addData("Drive Mode", "Field Centric");
                fieldCentricDriveRobot(gamepad1.left_stick_y, -gamepad1.left_stick_x, -gamepad1.right_stick_x, speed);
            } else {
                telemetry.addData("Drive Mode", "Tank");
                driveRobot(gamepad1.left_stick_y, -gamepad1.left_stick_x, -gamepad1.right_stick_x, speed);
            }

            // update running actions
            List<Action> newActions = new ArrayList<>();
            for (Action action : runningActions) {
                action.preview(packet.fieldOverlay());
                if (action.run(packet)) {
                    newActions.add(action);
                }
            }
            runningActions = newActions;

            dash.sendTelemetryPacket(packet);
            telemetry.update();
            sleep(20);
        }
        visionPortal.close();

    }
}
