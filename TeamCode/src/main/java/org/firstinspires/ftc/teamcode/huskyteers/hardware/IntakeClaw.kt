package org.firstinspires.ftc.teamcode.huskyteers.hardware

import com.qualcomm.robotcore.hardware.HardwareMap

class IntakeClaw(hardwareMap: HardwareMap) {
    private val leftRotatorServo = hardwareMap.servo["leftIntakeClawRotator"]
    private val rightRotatorServo = hardwareMap.servo["rightIntakeClawRotator"]
    private val grabberServo = hardwareMap.servo["intakeClawGrabber"]
    private val grabberRotatorServo = hardwareMap.servo["intakeClawGrabberRotator"]

    init {
        leftRotatorServo.scaleRange(0.0, 180.0)
        rightRotatorServo.scaleRange(0.0, 180.0)
        grabberRotatorServo.scaleRange(0.0, 360.0)
    }

    var rotatorAngle: Double
        get() = leftRotatorServo.position
        private set(value) {
            leftRotatorServo.position = value
            rightRotatorServo.position = value
        }

    var grabberPosition: Double
        get() = grabberServo.position
        private set(value) {
            grabberServo.position = value
        }

    var grabberRotatorAngle: Double
        get() = grabberRotatorServo.position
        set(value) {
            grabberRotatorServo.position = value
        }

    fun rotateDown() {
        rotatorAngle = 0.0
    }

    fun rotateUp() {
        rotatorAngle = 180.0
    }

    fun open() {
        grabberPosition = 0.0
    }

    fun close() {
        grabberPosition = 1.0
    }

    companion object {
        const val ROTATOR_TIME = 1.0
        const val GRAB_TIME = 1.0
    }
}