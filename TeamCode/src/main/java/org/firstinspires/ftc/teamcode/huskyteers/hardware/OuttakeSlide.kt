package org.firstinspires.ftc.teamcode.huskyteers.hardware

import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.Action
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.HardwareMap

class OuttakeSlide(hardwareMap: HardwareMap) {
    private val leftMotor = hardwareMap.dcMotor["leftSpeedOuttakeSlide"]
    private val rightMotor = hardwareMap.dcMotor["rightSpeedOuttakeSlide"]

    init {
        leftMotor.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        leftMotor.targetPosition = 0
        rightMotor.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        rightMotor.targetPosition = 0
    }

    val position = leftMotor.currentPosition

    var targetPosition: Int
        get() = leftMotor.targetPosition
        set(value) {
            leftMotor.mode = DcMotor.RunMode.RUN_TO_POSITION
            leftMotor.targetPosition = value
            rightMotor.mode = DcMotor.RunMode.RUN_TO_POSITION
            rightMotor.targetPosition = value
        }

    private fun extendTo(position: Int): Action {
        return object : Action {
            var initialized = false

            override fun run(p: TelemetryPacket): Boolean {
                if (!initialized) {
                    targetPosition = position
                    initialized = true
                }
                return leftMotor.isBusy
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