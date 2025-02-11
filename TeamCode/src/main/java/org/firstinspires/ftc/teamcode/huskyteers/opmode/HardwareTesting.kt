package org.firstinspires.ftc.teamcode.huskyteers.opmode

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx

@TeleOp(name = "Hardware Testing", group = "Testing")
class HardwareTesting : LinearOpMode() {
    override fun runOpMode() {
        waitForStart()
        if (isStopRequested) return

        val motor1 = hardwareMap.dcMotor["rightBack"] as DcMotorEx
        motor1.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        motor1.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE

        val motor2 = hardwareMap.dcMotor["rightFront"] as DcMotorEx
        motor2.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        motor2.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE

        var motor1CurrentPosition = motor1.currentPosition
        var motor1Running = false


        while (opModeIsActive() && !isStopRequested) {
            if (gamepad1.left_stick_y.toDouble() != 0.0) {
                motor1.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
                motor1.power = gamepad1.left_stick_y.toDouble()
            }
            motor2.power = gamepad1.right_stick_y.toDouble()
            if (motor1Running && gamepad1.left_stick_y.toDouble() == 0.0) {
                motor1CurrentPosition = motor1.currentPosition
                telemetry.addLine("Free")
            }
            if (gamepad1.left_stick_y.toDouble() == 0.0) {
                motor1.mode = DcMotor.RunMode.RUN_TO_POSITION
                motor1.power = 1.0
                motor1.velocity = 200.0
                motor1.targetPosition = motor1CurrentPosition
                telemetry.addData("Currently holding position", motor1CurrentPosition)
            }
            motor1Running = gamepad1.left_stick_y.toDouble() != 0.0
            telemetry.addData("Motor 1 Power", motor1.power)
            telemetry.addData("Motor 2 Power", motor2.power)
            telemetry.addData("Motor 1 Position", motor1.currentPosition)

            telemetry.update()
        }
    }

}
