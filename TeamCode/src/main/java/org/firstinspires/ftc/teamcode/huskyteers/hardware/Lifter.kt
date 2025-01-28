package org.firstinspires.ftc.teamcode.huskyteers.hardware

import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.Action
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.Servo

class Lifter(hardwareMap: HardwareMap) {
    private val motor = hardwareMap.get(DcMotorEx::class.java, "lifter")
    val servo = hardwareMap.get(Servo::class.java, "reelRotator")

    init {
        motor.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        motor.power = 1.0
        motor.targetPosition = 0
        motor.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
    }


    fun lift(): Action {
        return object : Action {
            private var initialized = false
            override fun run(p: TelemetryPacket): Boolean {
                if (!initialized) {
                    motor.targetPosition
                    initialized = true
                }

                return motor.isBusy
            }
        }
    }

    companion object {
        private const val LIFTED = 2000
    }
}