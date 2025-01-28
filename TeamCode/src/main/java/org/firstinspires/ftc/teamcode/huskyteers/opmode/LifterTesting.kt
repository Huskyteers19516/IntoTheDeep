package org.firstinspires.ftc.teamcode.huskyteers.opmode

import com.huskyteers.paths.StartInfo
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import org.firstinspires.ftc.teamcode.huskyteers.HuskyOpMode

@TeleOp
class LifterTesting :
    HuskyOpMode(StartInfo(StartInfo.Color.BLUE, StartInfo.Position.FarFromBasket)) {
    override fun runOpMode() {
        waitForStart()
        if (isStopRequested) return

        val lifterMotor = hardwareMap.dcMotor["lifter"]
        lifterMotor.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        while (opModeIsActive() && !isStopRequested) {
            lifterMotor.power =
                (if (gamepad1.dpad_up) 1.0 else 0.0) - (if (gamepad1.dpad_down) 1.0 else 0.0)
            telemetry.addData(
                "Lifter power",
                (if (gamepad1.dpad_up) 1.0 else 0.0) - (if (gamepad1.dpad_down) 1.0 else 0.0)
            )

            telemetry.addData("Lifter position", lifterMotor.currentPosition)
            telemetry.update()
            sleep(20)
        }
    }
}
