package org.firstinspires.ftc.teamcode.huskyteers.opmode

import com.acmerobotics.roadrunner.Action
import com.acmerobotics.roadrunner.InstantAction
import com.acmerobotics.roadrunner.ParallelAction
import com.acmerobotics.roadrunner.Pose2d
import com.acmerobotics.roadrunner.SequentialAction
import com.acmerobotics.roadrunner.SleepAction
import com.acmerobotics.roadrunner.TrajectoryActionBuilder
import com.acmerobotics.roadrunner.ftc.runBlocking
import com.huskyteers.paths.StartInfo
import com.huskyteers.paths.basketToCenterBrick
import com.huskyteers.paths.closeToBasketToRightmostBrick
import com.huskyteers.paths.toBasket
import com.huskyteers.paths.toParking
import org.firstinspires.ftc.teamcode.huskyteers.HuskyOpMode

class HuskyAuto(startInfo: StartInfo) : HuskyOpMode(startInfo) {
    private fun actionBuilder(startPose: Pose2d): TrajectoryActionBuilder =
        if (startInfo.color == StartInfo.Color.RED) drive.actionBuilder(startPose) else drive.oppositeActionBuilder(
            startPose
        )

    private fun toBasketAction(): Action =
        SequentialAction(
            ParallelAction(
                toBasket(actionBuilder(drive.localizer.pose))
                    .build(),
                SequentialAction(
                    InstantAction { bottomClaw.rotateUp() },
                    horizontalExtender.retract(),
                    InstantAction { topClaw.closeClaw() },
                    InstantAction { bottomClaw.openClaw() },
                    SleepAction(DELAY),
                    InstantAction { topClaw.rotateUp() },
                    verticalExtender.extend(),
                )
            ),
            InstantAction { topClaw.openClaw() },
            SleepAction(DELAY)
        )

    private fun toBrickAction(pathFunction: (TrajectoryActionBuilder) -> TrajectoryActionBuilder): Action =
        SequentialAction(
            actionBuilder(drive.localizer.pose)
                .run { pathFunction(this) }
                .build(),
            horizontalExtender.extend(),
            InstantAction { bottomClaw.closeClaw() },
            SleepAction(DELAY),
        )


    override fun runOpMode() {
        waitForStart()
        drive
        bottomClaw
        horizontalExtender
        verticalExtender
        topClaw
        if (isStopRequested) return


        if (startInfo.position == StartInfo.Position.CloseToBasket) {
            runBlocking(toBrickAction(::closeToBasketToRightmostBrick))
            runBlocking(toBasketAction())
            runBlocking(toBrickAction(::basketToCenterBrick))
            runBlocking(toBasketAction())
            runBlocking(toParking(actionBuilder(drive.localizer.pose)).build())
        }
    }

    companion object {
        const val DELAY = 1.0
    }
}
