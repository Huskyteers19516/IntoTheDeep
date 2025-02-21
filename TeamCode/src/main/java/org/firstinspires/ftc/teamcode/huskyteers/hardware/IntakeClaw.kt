package org.firstinspires.ftc.teamcode.huskyteers.hardware

import com.qualcomm.robotcore.hardware.HardwareMap

//@Config
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
        rotatorAngle = ROTATE_DOWN_ANGLE
    }

    fun rotateUp() {
        rotatorAngle = ROTATE_UP_ANGLE
    }

    fun open() {
        grabberPosition = OPEN_GRABBER_POSITION
    }

    fun close() {
        grabberPosition = CLOSE_GRABBER_POSITION
    }

    //    @Config
    companion object {
        @JvmField
        var ROTATOR_TIME = 1.0

        @JvmField
        var GRAB_TIME = 1.0

        @JvmField
        var ROTATE_DOWN_ANGLE = 0.0

        @JvmField
        var ROTATE_UP_ANGLE = 180.0

        @JvmField
        var OPEN_GRABBER_POSITION = 0.0

        @JvmField
        var CLOSE_GRABBER_POSITION = 1.0
    }
}