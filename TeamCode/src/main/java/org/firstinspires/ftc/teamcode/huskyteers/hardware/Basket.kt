package org.firstinspires.ftc.teamcode.huskyteers.hardware

import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.Servo

class Basket(hardwareMap: HardwareMap) {
    private val servo = hardwareMap.get(Servo::class.java, "basket")
    fun drop() {
        position = 0.5
    }

    fun raise() {
        position = 1.0

    }

    var position: Double
        get() = servo.position
        set(pos) {
            servo.position = pos
        }
}