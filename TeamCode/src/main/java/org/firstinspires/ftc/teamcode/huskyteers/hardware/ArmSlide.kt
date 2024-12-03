package org.firstinspires.ftc.teamcode.huskyteers.hardware

import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.Action
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.HardwareMap

class ArmSlide(hardwareMap: HardwareMap) {
    private val motor: DcMotor = hardwareMap.get(DcMotor::class.java, "armSlide")

    init {
        motor.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        motor.mode = DcMotor.RunMode.RUN_TO_POSITION
    }

    fun setPosition(position: Int) {
        motor.targetPosition = position
    }

    fun extendArm(): Action {
        return object : Action {
            private var initialized = false

            override fun run(p: TelemetryPacket): Boolean {
                if (!initialized) {
                    setPosition(EXTEND_POSITION)
                    initialized = true
                }
                return motor.isBusy
            }
        }
    }

    fun retractArm(): Action {
        return object : Action {
            private var initialized = false

            override fun run(p: TelemetryPacket): Boolean {
                if (!initialized) {
                    setPosition(RETRACT_POSITION)
                    initialized = true
                }
                return motor.isBusy
            }
        }
    }

    companion object {
        const val EXTEND_POSITION: Int = 100
        const val RETRACT_POSITION: Int = 0
    }
}
