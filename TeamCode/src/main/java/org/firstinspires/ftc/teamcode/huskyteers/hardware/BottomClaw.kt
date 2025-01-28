package org.firstinspires.ftc.teamcode.huskyteers.hardware

import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.Servo

class BottomClaw(hardwareMap: HardwareMap) {
    private val clawOpenerServo: Servo = hardwareMap.get(Servo::class.java, "bottomClawOpener")
    private val clawRotatorServo: Servo = hardwareMap.get(Servo::class.java, "bottomClawRotator")

//    init {
//        clawRotatorServo.scaleRange(-180.0, 180.0)
//    }

    fun rotateUp() {
        clawRotatorPosition = 1.0
    }

    fun rotateDown() {
        clawRotatorPosition = -1.0
    }

    fun openClaw() {
        clawOpenerPosition = OPEN_POSITION
    }

    fun closeClaw() {
        clawOpenerPosition = CLOSE_POSITION
    }

    private var clawOpenerPosition: Double
        get() = clawOpenerServo.position
        set(pos) {
            clawOpenerServo.position = pos
        }

    public var clawRotatorPosition: Double
        get() = clawRotatorServo.position
        set(pos) {
            clawRotatorServo.position = pos
        }

    companion object {
        private const val OPEN_POSITION = 1.0
        private const val CLOSE_POSITION = 0.0
    }
}
