package org.firstinspires.ftc.teamcode.huskyteers.opmode

import com.acmerobotics.roadrunner.ftc.runBlocking
import com.huskyteers.paths.StartInfo
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.teamcode.huskyteers.HuskyOpMode

@Autonomous
class BadAuto : HuskyOpMode(StartInfo(StartInfo.Color.BLUE, StartInfo.Position.None)) {

    private val elapsedTime = ElapsedTime()

    override fun runOpMode() {
        telemetry.addLine("THIS WILL MOVE IT STRAIGHT FORWARD 12 INCHES.")
        waitForStart()
        drive
        if (isStopRequested) return
        while (opModeIsActive() && !isStopRequested) {
            runBlocking(
                drive.actionBuilder(drive.localizer.pose)
                    .lineToY(12.0)
                    .build()
            )
        }
    }

    companion object {
        const val DELAY = 1.0
    }
}
