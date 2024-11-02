package org.firstinspires.ftc.teamcode.huskyteers.opmode;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.ftc.Actions;
//import com.huskyteers.paths.Paths;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.huskyteers.HuskyBot;

@Autonomous
public class HuskyAuto extends HuskyBot {
    @Override
    public void runOpMode() {
        instantiateMotors(new Pose2d(0, 0, 0));
        waitForStart();
        if (isStopRequested()) return;

        while (opModeIsActive() && !isStopRequested()) {
//            Actions.runBlocking(Paths.examplePath(drive.actionBuilder(drive.pose)).build());
        }
    }
}
