package org.firstinspires.ftc.teamcode.huskyteers.hardware

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
    fun extend() {
        position = EXTENDED
        state = State.EXTENDED
    }

    fun retract() {
        position = RETRACTED
        state = State.RETRACTED

    }

    var position: Int
        get() = motor.currentPosition
        set(pos) {
            motor.targetPosition = pos
        }
}