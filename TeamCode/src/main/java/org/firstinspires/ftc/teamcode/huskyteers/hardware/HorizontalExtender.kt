package org.firstinspires.ftc.teamcode.huskyteers.hardware

import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.Action
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.HardwareMap

class HorizontalExtender(hardwareMap: HardwareMap) {
    private val motor = hardwareMap.get(DcMotor::class.java, "horizontalExtender")
    init {
        motor.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        motor.mode = DcMotor.RunMode.RUN_TO_POSITION
    }
    val EXTENDED = 100
    val RETRACTED = 0

    fun extend(): Action {
        return object : Action {
            private var initialized = false

            override fun run(p: TelemetryPacket): Boolean {
                if (!initialized) {
                    this@HorizontalExtender.position = EXTENDED
                    initialized = true
                }

                return this@HorizontalExtender.position == EXTENDED
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

                return this@HorizontalExtender.position == RETRACTED
            }
        }
    }

    var position: Int
        get() = motor.currentPosition
        set(pos) {
            motor.targetPosition = pos
        }
}