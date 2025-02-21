package org.firstinspires.ftc.teamcode.huskyteers.hardware

import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.Action
import com.qualcomm.robotcore.hardware.HardwareMap

class IntakeSlide(hardwareMap: HardwareMap) {
    private val leftServo = hardwareMap.servo["leftIntakeSlide"]
    private val rightServo = hardwareMap.servo["rightIntakeSlide"]
    private val leftServoEncoder = hardwareMap.analogInput["leftIntakeSlideEncoder"]
    private val rightServoEncoder = hardwareMap.analogInput["rightIntakeSlideEncoder"]


    val position = leftServoEncoder.voltage / leftServoEncoder.maxVoltage

    var targetPosition: Double
        get() = leftServo.position
        set(value) {
            val limitedValue = value.coerceIn(
                INTAKE_SLIDE_RETRACTION,
                INTAKE_SLIDE_EXTENSION
            )
            leftServo.position = limitedValue
            rightServo.position = limitedValue
        }

    private fun extendTo(position: Double): Action {
        return object : Action {
            var initialized = false

            override fun run(p: TelemetryPacket): Boolean {
                if (!initialized) {
                    targetPosition = position
                    initialized = true
                }
                return this@IntakeSlide.position == position
            }
        }
    }

    fun extend(): Action {
        return extendTo(INTAKE_SLIDE_EXTENSION)
    }

    fun retract(): Action {
        return extendTo(INTAKE_SLIDE_RETRACTION)
    }

    //    @Config
    companion object {
        @JvmField
        var INTAKE_SLIDE_EXTENSION = 0.0

        @JvmField
        var INTAKE_SLIDE_RETRACTION = 1.0
    }
}

