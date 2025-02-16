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

        var motor1TargetPosition = motor1.currentPosition

        while (opModeIsActive() && !isStopRequested) {
            val motor1Power = gamepad1.left_stick_y.toDouble()
            val motor2Power = gamepad1.right_stick_y.toDouble()

            if (motor1Power != 0.0) {
                motor1.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
                motor1.power = motor1Power
                motor1TargetPosition = motor1.currentPosition
            } else {
                motor1.mode = DcMotor.RunMode.RUN_TO_POSITION
                motor1.targetPosition = motor1TargetPosition
                motor1.power = 1.0
            }

            motor2.power = motor2Power

            telemetry.addData("Motor 1 Power", motor1.power)
            telemetry.addData("Motor 2 Power", motor2.power)
            telemetry.addData("Motor 1 Position", motor1.currentPosition)
            telemetry.addData("Motor 1 Target Position", motor1TargetPosition)
            telemetry.update()
        }
    }
}