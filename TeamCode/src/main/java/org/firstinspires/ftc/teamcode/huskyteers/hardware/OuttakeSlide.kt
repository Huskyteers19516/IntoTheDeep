package org.firstinspires.ftc.teamcode.huskyteers.hardware

import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.Action
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.HardwareMap

class OuttakeSlide(hardwareMap: HardwareMap) {
    private val motor = hardwareMap.dcMotor["outtakeSlide"]

    init {
        motor.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        motor.targetPosition = 0
    }

    val position = motor.currentPosition

    var targetPosition: Int
        get() = motor.targetPosition
        set(value) {
            motor.mode = DcMotor.RunMode.RUN_TO_POSITION
            motor.targetPosition = value
        }

    private fun extendTo(position: Int): Action {
        return object : Action {
            var initialized = false

            override fun run(p: TelemetryPacket): Boolean {
                if (!initialized) {
                    targetPosition = position
                    initialized = true
                }
                return motor.isBusy
            }
        }
    }

    fun extendToHighBasket(): Action {
        return extendTo(HIGH_BASKET)
    }

    fun extendToLowBasket(): Action {
        return extendTo(LOW_BASKET)
    }

    fun retract(): Action {
        return extendTo(RETRACTED)
    }


    companion object {
        const val HIGH_BASKET = 3000
        const val LOW_BASKET = 2000
        const val RETRACTED = 0
    }
}