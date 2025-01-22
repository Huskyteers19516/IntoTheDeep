package org.firstinspires.ftc.teamcode.huskyteers.hardware

import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.Action
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.HardwareMap

class HorizontalExtender(hardwareMap: HardwareMap) {
    private val motor = hardwareMap.get(DcMotor::class.java, "horizontalExtender")

    init {
        motor.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        motor.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        motor.targetPosition = 0
        motor.mode = DcMotor.RunMode.RUN_USING_ENCODER
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

    private var position: Int
        get() = motor.currentPosition
        set(pos) {
            motor.targetPosition = pos
        }

    companion object {
        private const val EXTENDED = 2270
        private const val RETRACTED = 0
    }
}