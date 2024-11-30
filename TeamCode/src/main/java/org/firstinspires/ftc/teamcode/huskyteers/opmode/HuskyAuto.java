package org.firstinspires.ftc.teamcode.huskyteers.opmode;

import com.acmerobotics.roadrunner.Pose2d;

import org.firstinspires.ftc.teamcode.huskyteers.HuskyOpMode;
import org.firstinspires.ftc.teamcode.huskyteers.StartInfo;

public class HuskyAuto extends HuskyOpMode {
    public HuskyAuto(StartInfo startInfo) {
        super(startInfo);
    }

    @Override
    public void runOpMode() {
        instantiateMotors(new Pose2d(0, 0, 0));
        waitForStart();
        if (isStopRequested())
            return;

        while (opModeIsActive() && !isStopRequested()) {
            //            Actions.runBlocking(Paths.examplePath(drive.actionBuilder(drive.pose)).build());
        }
    }
}
