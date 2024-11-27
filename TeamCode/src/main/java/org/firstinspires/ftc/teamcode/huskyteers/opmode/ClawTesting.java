package org.firstinspires.ftc.teamcode.huskyteers.opmode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.huskyteers.HuskyOpMode;
import org.firstinspires.ftc.teamcode.huskyteers.StartInfo;
import org.firstinspires.ftc.teamcode.huskyteers.hardware.Claw;

import java.util.OptionalDouble;

@TeleOp
public class ClawTesting extends HuskyOpMode {
    @Override
    public void runOpMode() {
        initVisionPortal(StartInfo.Color.BLUE);
        claw = new Claw(hardwareMap);
        visionPortal.resumeLiveView();
        visionPortal.resumeStreaming();
        waitForStart();
        if (isStopRequested())
            return;

        while (opModeIsActive() && !isStopRequested()) {
            OptionalDouble sampleRotation = getSampleRotation();
            if (sampleRotation.isPresent()) {
                telemetry.addData("Sample rotation", sampleRotation);
                if (gamepad1.a) {
                    claw.rotateClaw(sampleRotation.getAsDouble());
                }
            } else {
                telemetry.addData("Sample rotation", "Sample not found");
            }

            telemetry.update();
            sleep(20);
        }
        visionPortal.close();

    }
}
