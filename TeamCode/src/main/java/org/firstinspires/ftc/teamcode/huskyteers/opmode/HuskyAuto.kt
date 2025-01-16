package org.firstinspires.ftc.teamcode.huskyteers.opmode

import com.acmerobotics.roadrunner.InstantAction
import com.acmerobotics.roadrunner.NullAction
import com.acmerobotics.roadrunner.SequentialAction
import com.acmerobotics.roadrunner.SleepAction
import com.acmerobotics.roadrunner.ftc.runBlocking
import com.huskyteers.paths.StartInfo
import com.huskyteers.paths.basketToCenterBrick
import com.huskyteers.paths.closeToBasketToRightmostBrick
import com.huskyteers.paths.toBasket
import org.firstinspires.ftc.teamcode.huskyteers.HuskyOpMode

class HuskyAuto(startInfo: StartInfo) : HuskyOpMode(startInfo) {
    override fun runOpMode() {
        waitForStart()
        drive
        topClaw
        if (isStopRequested) return

        val actionBuilder =
            if (startInfo.color == StartInfo.Color.RED) drive.actionBuilder(startInfo.position.pose2d) else drive.oppositeActionBuilder(
                startInfo.position.pose2d
            )
        val delay = 1.0

        if (startInfo.position == StartInfo.Position.CloseToBasket) {
            runBlocking(
                actionBuilder
                    .run {
                        closeToBasketToRightmostBrick(this)
                            .stopAndAdd(
                                SequentialAction(
                                    horizontalExtender.extend(),
                                    InstantAction { bottomClaw.closeClaw() },
                                    SleepAction(delay),
                                    horizontalExtender.retract(),
                                    InstantAction { bottomClaw.rotateUp() },
                                    SleepAction(delay),
                                    InstantAction { topClaw.closeClaw() },
                                    InstantAction { bottomClaw.openClaw() },
                                    SleepAction(delay),
                                    InstantAction { topClaw.rotateUp() },
                                    verticalExtender.extend(),
                                    InstantAction { topClaw.openClaw() },
                                    SleepAction(delay)
                                )
                            )
                    }
                    .run { toBasket(this).stopAndAdd(NullAction()) }
                    .run { basketToCenterBrick(this).stopAndAdd(NullAction()) }
                    .run { toBasket(this).stopAndAdd(NullAction()) }
                    .build()
            )
        }
    }
}
