package org.firstinspires.ftc.teamcode.huskyteers.utils

import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.Action


class FailoverAction(private val mainAction: Action, private val failoverAction: Action) : Action {
    private var failedOver = false

    override fun run(p: TelemetryPacket): Boolean {
        if (failedOver) {
            return failoverAction.run(p)
        }

        return mainAction.run(p)
    }

    fun failover() {
        failedOver = true
    }
}