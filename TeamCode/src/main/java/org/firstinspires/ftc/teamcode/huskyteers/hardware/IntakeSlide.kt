package org.firstinspires.ftc.teamcode.huskyteers.hardware

import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.Action
import com.qualcomm.robotcore.hardware.HardwareMap

class IntakeSlide(hardwareMap: HardwareMap) {
    private val leftServo = hardwareMap.servo["leftIntakeSlide"]
    private val rightServo = hardwareMap.servo["rightIntakeSlide"]
    private val leftServoEncoder = hardwareMap.analogInput["leftIntakeSlideEncoder"]
    private val rightServoEncoder = hardwareMap.analogInput["rightIntakeSlideEncoder"]


    val position = leftServoEncoder.voltage

    var targetPosition: Double
        get() = leftServo.position
        set(value) {
            leftServo.position = value
            rightServo.position = value
        }

    private fun extendTo(position: Double): Action {
        return object : Action {
            var initialized = false

            override fun run(p: TelemetryPacket): Boolean {
                if (!initialized) {
                    targetPosition = position
                    initialized = true
                }
                return leftServoEncoder.voltage == position
            }
        }
    }

    fun extend(): Action {
        return extendTo(EXTENDED)
    }

    fun retract(): Action {
        return extendTo(RETRACTED)
    }


    companion object {
        const val EXTENDED = 1.0
        const val RETRACTED = 0.0
    }
}