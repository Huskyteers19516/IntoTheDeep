package org.firstinspires.ftc.teamcode.huskyteers.hardware

import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.Servo

class Extender(hardwareMap: HardwareMap) {
    private val extenderServo = hardwareMap.get(Servo::class.java, "extender")
    fun extend() {
        position = 1.0
    }

    fun retract() {
        position = 0.0
        
    }

    var position: Double
        get() = extenderServo.position
        set(pos) {
            extenderServo.position = pos
        }
}