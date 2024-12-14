package org.firstinspires.ftc.teamcode.huskyteers.hardware

import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.Action
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.Servo
import kotlin.math.abs

class Claw(hardwareMap: HardwareMap) {
    private val clawOpenerServo: Servo = hardwareMap.get(Servo::class.java, "clawOpener")
    private val clawRotatorServo: Servo = hardwareMap.get(Servo::class.java, "clawRotator")

    /**
     * @param angle Angle of sample in degrees counterclockwise, with 0 being to the right
     * (like a unit circle)
     */
    fun rotateClaw(angle: Double) {
        clawRotatorPosition = angle
    }

    fun checkPosition(s: Double, dest: Double): Boolean {
        return abs(s - dest) <= eps
    }

    fun openClaw(): Action {
        return object : Action {
            private var initialized = false

            override fun run(p: TelemetryPacket): Boolean {
                if (!initialized) {
                    this@Claw.clawOpenerPosition = OPEN_POSITION
                    initialized = true
                }

                return checkPosition(this@Claw.clawOpenerPosition, OPEN_POSITION)
            }
        }
    }

    fun closeClaw(): Action {
        return object : Action {
            private var initialized = false
            private var previousPosition = 0.0
            private val timing = 10
            private var interval = 0

            override fun run(p: TelemetryPacket): Boolean {
                if (!initialized) {
                    this@Claw.clawOpenerPosition = CLOSE_POSITION
                    initialized = true
                }
                if (clawOpenerServo.position == previousPosition) {
                    this@Claw.clawOpenerPosition = previousPosition
                    return false
                }
                if (interval % timing == 0) {
                    previousPosition = this@Claw.clawOpenerPosition
                }
                interval++
                return checkPosition(this@Claw.clawOpenerPosition, CLOSE_POSITION)
            }
        }
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
        const val eps: Double = 1e-5
        private const val OPEN_POSITION = 1.0
        private const val CLOSE_POSITION = 0.0
    }
}
