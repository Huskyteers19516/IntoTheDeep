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

        extender
        while (opModeIsActive() && !isStopRequested) {
            if (gamepad1.a) {
                extender.extend()
            } else if (gamepad1.b) {
                extender.retract()
            }
            telemetry.addData("Extender position", extender.position)
            telemetry.update()
            sleep(20)
        }
        visionPortal.close()
    }
}
