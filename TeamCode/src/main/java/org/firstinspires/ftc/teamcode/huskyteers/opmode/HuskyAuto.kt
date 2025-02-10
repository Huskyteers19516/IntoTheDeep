package org.firstinspires.ftc.teamcode.huskyteers.opmode

import com.acmerobotics.roadrunner.Pose2d
import com.acmerobotics.roadrunner.TrajectoryActionBuilder
import com.acmerobotics.roadrunner.Vector2d
import com.acmerobotics.roadrunner.ftc.runBlocking
import com.huskyteers.paths.StartInfo
import com.huskyteers.paths.basketToLeftmostBrick
import com.huskyteers.paths.closeToBasketToRightmostBrick
import org.firstinspires.ftc.teamcode.huskyteers.HuskyOpMode

class HuskyAuto(startInfo: StartInfo) : HuskyOpMode(startInfo) {
    private fun actionBuilder(startPose: Pose2d): TrajectoryActionBuilder =
        if (startInfo.color == StartInfo.Color.RED) drive.actionBuilder(startPose) else drive.oppositeActionBuilder(
            startPose
        )

    override fun runOpMode() {
        waitForStart()
        if (isStopRequested) return
        runBlocking(
            actionBuilder(drive.localizer.pose)
                .run {
                    closeToBasketToRightmostBrick(this)
                }
                .run {
                    basketToLeftmostBrick(this, intakeClaw::rotateClaw)
                }
                .build()
        )
    }

    companion object {
        const val DELAY = 1.0
    }
}
