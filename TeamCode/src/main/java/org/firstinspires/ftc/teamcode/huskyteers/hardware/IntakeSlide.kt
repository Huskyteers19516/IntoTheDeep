package org.firstinspires.ftc.teamcode.huskyteers.hardware

import com.acmerobotics.roadrunner.Action
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareMap

class IntakeSlide(hardwareMap: HardwareMap) {
    private val leftServo = hardwareMap.crservo["leftIntakeSlide"]
    private val rightServo = hardwareMap.crservo["rightIntakeSlide"]
    private val leftServoEncoder = hardwareMap.analogInput["leftIntakeSlideEncoder"]
    private val rightServoEncoder = hardwareMap.analogInput["rightIntakeSlideEncoder"]
    val zeroSensor = hardwareMap.touchSensor["intakeSlideZeroSensor"]

    init {
        leftServo.direction = DcMotorSimple.Direction.REVERSE
    }


    val position: Double
        get() = leftServoEncoder.voltage / leftServoEncoder.maxVoltage

    var power: Double
        get() = leftServo.power
        set(value) {
            leftServo.power = value
            rightServo.power = value
        }

    fun retract(): Action {
        return Action {
            power = -1.0
            zeroSensor.isPressed
        }
    }

    fun extend(): Action {
        return Action {
            power = 1.0
            !zeroSensor.isPressed
        }
    }
}

