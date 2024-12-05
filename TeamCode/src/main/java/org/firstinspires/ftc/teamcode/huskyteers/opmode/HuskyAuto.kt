package org.firstinspires.ftc.teamcode.huskyteers.opmode

import com.acmerobotics.roadrunner.ftc.runBlocking
import com.huskyteers.paths.StartInfo
import com.huskyteers.paths.closeToBasketToRightmostBrick
import org.firstinspires.ftc.teamcode.huskyteers.HuskyOpMode

class HuskyAuto(startInfo: StartInfo) : HuskyOpMode(startInfo) {
    override fun runOpMode() {
        waitForStart()
        drive
        claw
        if (isStopRequested) return

        if (startInfo.position == StartInfo.Position.CloseToBasket) {
            runBlocking(closeToBasketToRightmostBrick(drive.actionBuilder(drive.pose)).build())
        }
    }
}
