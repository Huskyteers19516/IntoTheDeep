package org.firstinspires.ftc.teamcode.huskyteers.opmode

import com.huskyteers.paths.StartInfo
import org.firstinspires.ftc.teamcode.huskyteers.HuskyOpMode

class HuskyAuto(startInfo: StartInfo) : HuskyOpMode(startInfo) {
    override fun runOpMode() {
        waitForStart()
        if (isStopRequested) return

        while (opModeIsActive() && !isStopRequested) {
        }
    }
}
