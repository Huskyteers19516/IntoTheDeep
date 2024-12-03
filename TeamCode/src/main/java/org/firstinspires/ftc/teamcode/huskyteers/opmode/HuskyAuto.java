package org.firstinspires.ftc.teamcode.huskyteers.opmode;

import org.firstinspires.ftc.teamcode.huskyteers.HuskyOpMode;
import org.firstinspires.ftc.teamcode.huskyteers.utils.StartInfo;

public class HuskyAuto extends HuskyOpMode {
    public HuskyAuto(StartInfo startInfo) {
        super(startInfo);
    }

    @Override
    public void runOpMode() {
        instantiateMotors(startInfo.position.pose2d);
        waitForStart();
        if (isStopRequested())
            return;

        while (opModeIsActive() && !isStopRequested()) {

        }
    }
}
