package org.firstinspires.ftc.teamcode.huskyteers.hardware

import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.Action
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.HardwareMap

class VerticalExtender(hardwareMap: HardwareMap) {
    private val motor = hardwareMap.get(DcMotorEx::class.java, "verticalExtender")

    init {
        motor.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER;
        motor.targetPosition = 0
        motor.mode = DcMotor.RunMode.RUN_USING_ENCODER
        motor.power = 0.7
        motor.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
    }

    fun extend(): Action {
        return object : Action {
            private var initialized = false

            override fun run(p: TelemetryPacket): Boolean {
                if (!initialized) {
                    this@VerticalExtender.position = EXTENDED
                    initialized = true
                }

                return this@VerticalExtender.motor.isBusy
            }
        }
    }

    fun retract(): Action {
        return object : Action {
            private var initialized = false

            override fun run(p: TelemetryPacket): Boolean {
                if (!initialized) {
                    this@VerticalExtender.position = RETRACTED
                    initialized = true
                }

                return this@VerticalExtender.motor.isBusy
            }
        }
    }

    var position: Int
        get() = motor.currentPosition
        set(pos) {
            motor.targetPosition = pos
        }

    companion object {
        private const val EXTENDED = -3497
        private const val RETRACTED = 0
    }
}