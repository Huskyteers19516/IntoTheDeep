package org.firstinspires.ftc.teamcode.huskyteers.opmode

import com.acmerobotics.roadrunner.*
import com.acmerobotics.roadrunner.ftc.runBlocking
import com.huskyteers.paths.*
import org.firstinspires.ftc.teamcode.huskyteers.HuskyOpMode
import org.firstinspires.ftc.teamcode.huskyteers.hardware.IntakeClaw

class HuskyAuto(startInfo: StartInfo) : HuskyOpMode(startInfo) {
    private fun actionBuilder(startPose: Pose2d): TrajectoryActionBuilder =
        if (startInfo.color == StartInfo.Color.RED) drive.actionBuilder(startPose) else drive.oppositeActionBuilder(
            startPose
        )

    fun rotateIntakeGrabber(angle: Double) = SequentialAction(
        InstantAction { intakeClaw.grabberRotatorAngle = angle },
        SleepAction(IntakeClaw.ROTATOR_TIME)
    )

    override fun runOpMode() {
        waitForStart()
        if (isStopRequested) return
        runBlocking(
            actionBuilder(drive.localizer.pose)
                .run {
                    closeToBasketToRightmostBrick(this)
                }
                .run { toBasket(this) }
                .run { basketToCenterBrick(this) }
                .run { toBasket(this) }
                .run { basketToLeftmostBrick(this, ::rotateIntakeGrabber) }
                .run { toBasket(this) }
                .run { toParking(this) }
                .build()
        )
    }
}
