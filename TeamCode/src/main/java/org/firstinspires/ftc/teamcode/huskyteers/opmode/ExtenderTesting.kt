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

        val horizontalExtenderMotor = hardwareMap.dcMotor["horizontalExtender"]
        val verticalExtenderMotor = hardwareMap.dcMotor["verticalExtender"]
        while (opModeIsActive() && !isStopRequested) {
            horizontalExtenderMotor.power = -gamepad1.left_stick_y.toDouble()
            verticalExtenderMotor.power = -gamepad1.right_stick_y.toDouble()

            telemetry.addData(
                "Horizontal extender position",
                horizontalExtenderMotor.currentPosition
            )
            telemetry.addData("Vertical extender position", verticalExtenderMotor.currentPosition)
            telemetry.update()
            sleep(20)
        }
        visionPortal.close()
    }
}
