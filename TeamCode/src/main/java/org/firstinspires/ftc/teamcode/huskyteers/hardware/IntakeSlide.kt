package org.firstinspires.ftc.teamcode.huskyteers.hardware

import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.Action
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.HardwareMap

class IntakeSlide(hardwareMap: HardwareMap) {
    private val servo1 = hardwareMap.servo["intakeSlide1"]
    private val servo2 = hardwareMap.servo["intakeSlide2"]
    private val servoEncoder1 = hardwareMap.analogInput["intakeSlideEncoder1"]
    private val servoEncoder2 = hardwareMap.analogInput["intakeSlideEncoder2"]


    val position = servoEncoder1.voltage

    var targetPosition: Double
        get() = servo1.position
        set(value) {
            servo1.position = value
            servo2.position = value
        }

    private fun extendTo(position: Double): Action {
        return object : Action {
            var initialized = false

            override fun run(p: TelemetryPacket): Boolean {
                if (!initialized) {
                    targetPosition = position
                    initialized = true
                }
                return servoEncoder1.voltage == position
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