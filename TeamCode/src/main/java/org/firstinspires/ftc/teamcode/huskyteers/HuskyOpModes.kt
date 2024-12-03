package org.firstinspires.ftc.teamcode.huskyteers

import com.huskyteers.paths.StartInfo
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.OpModeManager
import com.qualcomm.robotcore.eventloop.opmode.OpModeRegistrar
import org.firstinspires.ftc.robotcore.internal.opmode.OpModeMeta
import org.firstinspires.ftc.teamcode.huskyteers.opmode.HuskyAuto
import org.firstinspires.ftc.teamcode.huskyteers.opmode.HuskyTeleOp

const val GROUP: String = "huskyteers"
const val DISABLED: Boolean = false

private fun metaForTeleOp(
    cls: Class<out OpMode?>,
    startInfo: StartInfo,
    autoCls: Class<out OpMode?>
): OpModeMeta {
    return OpModeMeta.Builder()
        .setName(cls.simpleName + " - " + startInfo.toString())
        .setGroup(GROUP)
        .setFlavor(OpModeMeta.Flavor.TELEOP)
        .setTransitionTarget(autoCls.simpleName + " - " + startInfo.toString())
        .build()
}

private fun metaForAuto(cls: Class<out OpMode?>, startInfo: StartInfo): OpModeMeta {
    return OpModeMeta.Builder()
        .setName(cls.simpleName + " - " + startInfo.toString())
        .setGroup(GROUP)
        .setFlavor(OpModeMeta.Flavor.AUTONOMOUS)
        .build()
}

@OpModeRegistrar
fun register(manager: OpModeManager) {
    if (DISABLED) return

    for (position in StartInfo.Position.entries.toTypedArray()) {
        for (color in StartInfo.Color.entries.toTypedArray()) {
            val startConfiguration = StartInfo(color, position)
            manager.register(
                metaForTeleOp(
                    HuskyTeleOp::class.java, startConfiguration,
                    HuskyAuto::class.java
                ), HuskyTeleOp(startConfiguration)
            )
            manager.register(
                metaForAuto(HuskyAuto::class.java, startConfiguration),
                HuskyAuto(startConfiguration)
            )
        }
    }
}

