package org.firstinspires.ftc.teamcode.huskyteers.opmode

import com.acmerobotics.roadrunner.PoseVelocity2d
import com.acmerobotics.roadrunner.Vector2d
import com.huskyteers.paths.StartInfo
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.teamcode.huskyteers.HuskyOpMode

@Autonomous
class BadAuto : HuskyOpMode(StartInfo(StartInfo.Color.BLUE, StartInfo.Position.None)) {

    private val elapsedTime = ElapsedTime()

    override fun runOpMode() {
        waitForStart()
        drive
        bottomClaw
        horizontalExtender
        verticalExtender
        topClaw
        if (isStopRequested) return
        while (opModeIsActive() && !isStopRequested) {
            drive.setDrivePowers(PoseVelocity2d(Vector2d(0.0, 10.0), 0.0))
        }
    }

    companion object {
        const val DELAY = 1.0
    }
}
