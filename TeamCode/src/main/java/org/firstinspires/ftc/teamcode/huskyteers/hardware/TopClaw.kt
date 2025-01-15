package org.firstinspires.ftc.teamcode.huskyteers.hardware

import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.Action
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.Servo
import kotlin.math.abs

class TopClaw(hardwareMap: HardwareMap) {
    private val clawOpenerServo: Servo = hardwareMap.get(Servo::class.java, "topClawOpener")
    private val clawRotatorServo: Servo = hardwareMap.get(Servo::class.java, "topClawRotator")
    init {
        clawRotatorServo.scaleRange(-180.0, 180.0)
    }

    fun openClaw() {
        clawOpenerPosition = OPEN_POSITION
    }

    fun closeClaw() {
        clawOpenerPosition = CLOSE_POSITION
    }


    var clawOpenerPosition: Double
        get() = clawOpenerServo.position
        set(pos) {
            clawOpenerServo.position = pos
        }

    var clawRotatorPosition: Double
        get() = clawRotatorServo.position
        set(pos) {
            clawRotatorServo.position = pos
        }

    companion object {
        private const val OPEN_POSITION = 1.0
        private const val CLOSE_POSITION = 0.0
    }
}
