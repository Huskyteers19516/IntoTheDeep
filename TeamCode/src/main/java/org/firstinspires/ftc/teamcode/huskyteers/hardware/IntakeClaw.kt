package org.firstinspires.ftc.teamcode.huskyteers.hardware

import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.Action
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.util.ElapsedTime

class IntakeClaw(hardwareMap: HardwareMap) {
    private val rotatorServo1 = hardwareMap.servo["intakeClawRotator1"]
    private val rotatorServo2 = hardwareMap.servo["intakeClawRotator2"]
    private val grabberServo = hardwareMap.servo["intakeClawGrabber"]
    private val grabberRotatorServo = hardwareMap.servo["intakeClawGrabberRotator"]

    init {
        rotatorServo1.scaleRange(0.0, 180.0)
        rotatorServo2.scaleRange(0.0, 180.0)
        grabberRotatorServo.scaleRange(0.0, 360.0)
    }

    var rotatorAngle: Double
        get() = rotatorServo1.position
        set(value) {
            rotatorServo1.position = value
            rotatorServo2.position = value
        }

    var grabberPosition: Double
        get() = grabberServo.position
        set(value) {
            grabberServo.position = value
        }

    var grabberRotatorAngle: Double
        get() = grabberRotatorServo.position
        set(value) {
            grabberRotatorServo.position = value
        }

    fun rotateClaw(angle: Double): Action {
        return object : Action {
            var initialized = false
            var elapsedTime = ElapsedTime()

            override fun run(p: TelemetryPacket): Boolean {
                if (!initialized) {
                    rotatorAngle = angle
                    initialized = true
                    elapsedTime.reset()
                }
                return elapsedTime.time() < ROTATOR_TIME
            }
        }
    }

    companion object {
        const val ROTATOR_TIME = 1.0
    }
}