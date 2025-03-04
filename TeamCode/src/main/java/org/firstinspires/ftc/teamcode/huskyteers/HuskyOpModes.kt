package org.firstinspires.ftc.teamcode.huskyteers

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.config.reflection.ReflectionConfig
import com.acmerobotics.dashboard.config.variable.CustomVariable
import com.huskyteers.paths.StartInfo
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.OpModeManager
import com.qualcomm.robotcore.eventloop.opmode.OpModeRegistrar
import org.firstinspires.ftc.robotcore.internal.opmode.OpModeMeta
import org.firstinspires.ftc.teamcode.huskyteers.hardware.IntakeClaw
import org.firstinspires.ftc.teamcode.huskyteers.hardware.IntakeSlide
import org.firstinspires.ftc.teamcode.huskyteers.hardware.OuttakeClaw
import org.firstinspires.ftc.teamcode.huskyteers.hardware.OuttakeSlide
import org.firstinspires.ftc.teamcode.huskyteers.opmode.HuskyAuto
import org.firstinspires.ftc.teamcode.huskyteers.opmode.HuskyTeleOp

const val GROUP: String = "huskyteers"
const val DISABLED: Boolean = false

private fun metaForTeleOp(
    cls: Class<out OpMode?>,
    startInfo: StartInfo,
): OpModeMeta {
    return OpModeMeta.Builder()
        .setName(cls.simpleName + " - " + startInfo.color.name)
        .setGroup(GROUP)
        .setFlavor(OpModeMeta.Flavor.TELEOP)
        .build()
}

private fun metaForAuto(cls: Class<out OpMode?>, startInfo: StartInfo, teleOpCls: Class<out OpMode?>): OpModeMeta {
    return OpModeMeta.Builder()
        .setName(cls.simpleName + " - " + startInfo.toString())
        .setGroup(GROUP)
        .setFlavor(OpModeMeta.Flavor.AUTONOMOUS)
        .setTransitionTarget(teleOpCls.simpleName + " - " + startInfo.color.name)
        .build()
}

@OpModeRegistrar
fun register(manager: OpModeManager) {
    if (DISABLED) return

    for (color in StartInfo.Color.entries.toTypedArray()) {
        val teleOpStartConfiguration = StartInfo(color, StartInfo.Position.None)
        manager.register(
            metaForTeleOp(
                HuskyTeleOp::class.java, teleOpStartConfiguration,
            ), HuskyTeleOp(teleOpStartConfiguration)
        )

        for (position in StartInfo.Position.entries.toTypedArray()) {
            val startConfiguration = StartInfo(color, position)
            manager.register(
                metaForAuto(
                    HuskyAuto::class.java, startConfiguration, HuskyAuto::class.java
                ),
                HuskyAuto(startConfiguration)
            )
        }
        FtcDashboard.getInstance().withConfigRoot { configRoot: CustomVariable ->
            for (c in listOf(
                HuskyTeleOp::class.java,
                OuttakeClaw::class.java,
                IntakeClaw::class.java,
                OuttakeSlide::class.java,
                IntakeSlide::class.java,
            )) {
                configRoot.putVariable(
                    c.simpleName,
                    ReflectionConfig.createVariableFromClass(c)
                )
            }
        }

    }
}

