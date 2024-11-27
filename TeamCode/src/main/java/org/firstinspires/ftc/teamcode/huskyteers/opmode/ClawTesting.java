package org.firstinspires.ftc.teamcode.huskyteers.opmode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.huskyteers.HuskyOpMode;
import org.firstinspires.ftc.teamcode.huskyteers.StartInfo;
import org.firstinspires.ftc.teamcode.huskyteers.hardware.ArmSlide;
import org.firstinspires.ftc.teamcode.huskyteers.utils.GamepadUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@TeleOp
public class HuskyTeleOp extends HuskyOpMode {
    @Override
    public void runOpMode() {
        initVisionPortal(StartInfo.Color.BLUE);
        claw = new Claw(hardwareMap);
        waitForStart();
        if (isStopRequested())
            return;
        
        

        while (opModeIsActive() && !isStopRequested()) {
            if (gamepad1.a){ alignClawToSample();}
            telemetry.update();
            sleep(20);
        }
        visionPortal.close();

    }
}
