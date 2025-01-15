package org.firstinspires.ftc.teamcode.huskyteers.hardware

import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.Action
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.HardwareMap

class VerticalExtender(hardwareMap: HardwareMap) {
    private val motor = hardwareMap.get(DcMotor::class.java, "verticalExtender")
    var state = State.RETRACTED

    enum class State {
        EXTENDED, RETRACTED
    }

    init {
        motor.mode = DcMotor.RunMode.RUN_USING_ENCODER
        motor.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
    }

    private val EXTENDED = 300;
    private val RETRACTED = 0;
    fun extend(): Action {
        return object : Action {
            private var initialized = false

            override fun run(p: TelemetryPacket): Boolean {
                if (!initialized) {
                    this@VerticalExtender.position = EXTENDED
                    initialized = true
                }

                return this@VerticalExtender.position == EXTENDED
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

                return this@VerticalExtender.position == RETRACTED
            }
        }
    }
    var position: Int
        get() = motor.currentPosition
        set(pos) {
            motor.targetPosition = pos
        }
}