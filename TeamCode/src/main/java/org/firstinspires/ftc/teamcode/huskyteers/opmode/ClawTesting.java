package org.firstinspires.ftc.teamcode.huskyteers.opmode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.huskyteers.HuskyOpMode;
import org.firstinspires.ftc.teamcode.huskyteers.StartInfo;
import org.firstinspires.ftc.teamcode.huskyteers.hardware.Claw;

@TeleOp
public class ClawTesting extends HuskyOpMode {
    @Override
    public void runOpMode() {
        initVisionPortal(StartInfo.Color.BLUE);
        claw = new Claw(hardwareMap);
        waitForStart();
        if (isStopRequested())
            return;

        while (opModeIsActive() && !isStopRequested()) {
            if (gamepad1.a) {
                alignClawToSample();
            }
            telemetry.update();
            sleep(20);
        }
        visionPortal.close();

    }
}
