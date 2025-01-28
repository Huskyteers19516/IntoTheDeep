package org.firstinspires.ftc.teamcode.huskyteers.hardware

import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.Action
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.HardwareMap

class HorizontalExtender(hardwareMap: HardwareMap) {
    val motor = hardwareMap.get(DcMotorEx::class.java, "horizontalExtender")

    init {
        motor.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        motor.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        motor.power = 0.7
        motor.targetPosition = 0
        motor.mode = DcMotor.RunMode.RUN_TO_POSITION
        motor.velocity = 50.0
    }

    fun extend(): Action {
        return object : Action {
            private var initialized = false

            override fun run(p: TelemetryPacket): Boolean {
                if (!initialized) {
                    this@HorizontalExtender.position = EXTENDED
                    initialized = true
                }

                return this@HorizontalExtender.position != EXTENDED
            }
        }
    }

    fun retract(): Action {
        return object : Action {
            private var initialized = false

            override fun run(p: TelemetryPacket): Boolean {
                if (!initialized) {
                    this@HorizontalExtender.position = RETRACTED
                    initialized = true
                }

                return this@HorizontalExtender.position != RETRACTED
            }
        }
    }

    var power: Double
        get() = motor.power
        set(p) {
            motor.power = p
        }

    var runMode: DcMotor.RunMode
        get() = motor.mode
        set(mode) {
            motor.mode = mode
        }

    var position: Int
        get() = motor.currentPosition
        set(pos) {
            motor.targetPosition = pos
        }

    companion object {
        const val MAX_EXTENDED = 1500
        private const val EXTENDED = 1776
        private const val RETRACTED = 0
    }
}