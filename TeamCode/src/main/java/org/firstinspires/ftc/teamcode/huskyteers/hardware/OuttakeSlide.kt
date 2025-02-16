package org.firstinspires.ftc.teamcode.huskyteers.hardware

import com.acmerobotics.dashboard.config.Config
import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.Action
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.HardwareMap

@Config
class OuttakeSlide(hardwareMap: HardwareMap) {
    private val leftSpeedMotor = hardwareMap.dcMotor["leftSpeedOuttakeSlide"]
    private val rightSpeedMotor = hardwareMap.dcMotor["rightSpeedOuttakeSlide"]
    private val leftTorqueMotor = hardwareMap.dcMotor["leftTorqueOuttakeSlide"]
    private val rightTorqueMotor = hardwareMap.dcMotor["rightTorqueOuttakeSlide"]

    init {
        arrayOf(leftSpeedMotor, rightSpeedMotor, leftTorqueMotor, rightTorqueMotor).forEach {
            it.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
            it.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
            it.targetPosition = 0
        }
    }

    val position = leftSpeedMotor.currentPosition

    var targetPosition: Int
        get() = leftSpeedMotor.targetPosition
        set(value) {
            val limitedValue = value.coerceIn(RETRACTED, HIGH_BASKET)
            leftSpeedMotor.mode = DcMotor.RunMode.RUN_TO_POSITION
            rightSpeedMotor.mode = DcMotor.RunMode.RUN_TO_POSITION
            leftSpeedMotor.targetPosition = limitedValue
            rightSpeedMotor.targetPosition = limitedValue
        }

    var speedMotorSpeed: Double
        get() = leftSpeedMotor.power
        set(value) {
            arrayOf(leftSpeedMotor, rightSpeedMotor).forEach {
                it.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
            }
            leftSpeedMotor.power = value
            rightSpeedMotor.power = value
        }

    var allMotorSpeed: Double
        get() = leftSpeedMotor.power
        set(value) {
            arrayOf(leftSpeedMotor, rightSpeedMotor, leftTorqueMotor, rightTorqueMotor).forEach {
                it.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
            }
            leftSpeedMotor.power = value
            rightSpeedMotor.power = value
            leftTorqueMotor.power = value
            rightTorqueMotor.power = value
        }

    private fun extendTo(position: Int): Action {
        return object : Action {
            var initialized = false

            override fun run(p: TelemetryPacket): Boolean {
                if (!initialized) {
                    targetPosition = position
                    initialized = true
                }
                return leftSpeedMotor.isBusy
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


    @Config
    companion object {
        // TODO: Adjust limits
        @JvmField
        var HIGH_BASKET = 3000
        @JvmField
        var LOW_BASKET = 2000
        @JvmField
        var RETRACTED = 0
    }
}