package org.firstinspires.ftc.teamcode.huskyteers.opmode

import com.huskyteers.paths.StartInfo
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import org.firstinspires.ftc.teamcode.huskyteers.HuskyOpMode

@TeleOp
class ExtenderTesting :
    HuskyOpMode(StartInfo(StartInfo.Color.BLUE, StartInfo.Position.FarFromBasket)) {
    override fun runOpMode() {
        waitForStart()
        if (isStopRequested) return

        val horizontalExtenderMotor = hardwareMap.dcMotor["horizontalExtender"]
        val verticalExtenderMotor = hardwareMap.dcMotor["verticalExtender"]
        val lifterMotor = hardwareMap.dcMotor["lifter"]
        horizontalExtenderMotor.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        verticalExtenderMotor.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        lifterMotor.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        horizontalExtenderMotor.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        verticalExtenderMotor.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        lifterMotor.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        while (opModeIsActive() && !isStopRequested) {
            horizontalExtenderMotor.power = -gamepad1.left_stick_y.toDouble()
            verticalExtenderMotor.power = -gamepad1.right_stick_y.toDouble()
            lifterMotor.power =
                (if (gamepad1.dpad_up) 1.0 else 0.0) - (if (gamepad1.dpad_down) 1.0 else 0.0)

            telemetry.addData(
                "Horizontal extender position",
                horizontalExtenderMotor.currentPosition
            )
            telemetry.addData("Vertical extender position", verticalExtenderMotor.currentPosition)
            telemetry.addData("Lifter position", lifterMotor.currentPosition)
            telemetry.update()
            sleep(20)
        }
    }
}
