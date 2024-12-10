package org.firstinspires.ftc.teamcode.huskyteers.opmode

import com.huskyteers.paths.StartInfo
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.huskyteers.HuskyOpMode

@TeleOp
class ClawTesting : HuskyOpMode(StartInfo(StartInfo.Color.BLUE, StartInfo.Position.FarFromBasket)) {
    override fun runOpMode() {
        visionPortal.resumeLiveView()
        visionPortal.resumeStreaming()
        waitForStart()
        if (isStopRequested) return

        while (opModeIsActive() && !isStopRequested) {
            val sampleRotation = sampleRotation
            if (sampleRotation.isPresent) {
                telemetry.addData("Dumb sample rotation", dumbSampleRotation)
                telemetry.addData("Sample rotation", sampleRotation)
                if (gamepad1.a) {
                    claw.rotateClaw(sampleRotation.asDouble)
                }
            } else {
                telemetry.addData("Sample rotation", "Sample not found")
            }

            telemetry.update()
            sleep(20)
        }
        visionPortal.close()
    }
}
