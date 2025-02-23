package org.firstinspires.ftc.teamcode.huskyteers.opmode

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import org.firstinspires.ftc.teamcode.huskyteers.utils.GamepadUtils

@TeleOp(name = "Hardware Testing", group = "Testing")
class HardwareTesting : LinearOpMode() {
    override fun runOpMode() {
        waitForStart()
        if (isStopRequested) return

        val servo = hardwareMap.servo["outtakeClawRotator"]
        val servo2 = hardwareMap.servo["intakeClawGrabber"]
        val servo3 = hardwareMap.servo["outtakeClawGrabber"]
        val motor = hardwareMap.dcMotor["rightSpeedOuttakeSlide"]
        val motor2 = hardwareMap.dcMotor["leftSpeedOuttakeSlide"]
        val gamepad1Utils = GamepadUtils()

        servo3.position = 0.0

        gamepad1Utils.addRisingEdge("x") {
            servo3.position += 0.01
        }
        gamepad1Utils.addRisingEdge("y") {
            servo3.position -= 0.01
        }


        while (opModeIsActive() && !isStopRequested) {
            gamepad1Utils.processUpdates(gamepad1)

            servo.position += if (gamepad1.dpad_up) 0.01 else 0.0 - (if (gamepad1.dpad_down) 0.01 else 0.0)
            telemetry.addData("Position", servo.position)
            telemetry.addData("Servo 3 Position", servo3.position)
            telemetry.update()
            servo2.position = 0.0
            motor.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
            motor2.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
            motor.power = gamepad1.left_stick_y.toDouble()
            motor2.power = gamepad1.left_stick_y.toDouble()
        }
    }
}