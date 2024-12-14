package org.firstinspires.ftc.teamcode.huskyteers.opmode

import com.huskyteers.paths.StartInfo
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.huskyteers.HuskyOpMode

@TeleOp
class ExtenderTesting :
    HuskyOpMode(StartInfo(StartInfo.Color.BLUE, StartInfo.Position.FarFromBasket)) {
    override fun runOpMode() {
        waitForStart()
        if (isStopRequested) return

        horizontalExtender
        while (opModeIsActive() && !isStopRequested) {
            if (gamepad1.a) {
                horizontalExtender.extend()
            } else if (gamepad1.b) {
                horizontalExtender.retract()
            }
            telemetry.addData("Extender position", horizontalExtender.position)
            telemetry.update()
            sleep(20)
        }
        visionPortal.close()
    }
}
