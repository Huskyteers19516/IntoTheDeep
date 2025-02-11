package org.firstinspires.ftc.teamcode.huskyteers.hardware

import com.qualcomm.robotcore.hardware.HardwareMap

class OuttakeClaw(hardwareMap: HardwareMap) {
    private val leftRotatorServo = hardwareMap.servo["leftOuttakeClawRotator"]
    private val rightRotatorServo = hardwareMap.servo["rightOuttakeClawRotator"]
    private val grabberServo = hardwareMap.servo["outtakeClawGrabber"]

    init {
        leftRotatorServo.scaleRange(0.0, 180.0)
        rightRotatorServo.scaleRange(0.0, 180.0)
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