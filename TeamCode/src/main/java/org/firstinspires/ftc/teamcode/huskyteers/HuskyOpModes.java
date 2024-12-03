package org.firstinspires.ftc.teamcode.huskyteers;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpModeManager;
import com.qualcomm.robotcore.eventloop.opmode.OpModeRegistrar;

import org.firstinspires.ftc.robotcore.internal.opmode.OpModeMeta;
import org.firstinspires.ftc.teamcode.huskyteers.opmode.HuskyAuto;
import org.firstinspires.ftc.teamcode.huskyteers.opmode.HuskyTeleOp;
import org.firstinspires.ftc.teamcode.huskyteers.utils.StartInfo;

@SuppressWarnings("unused")
public final class HuskyOpModes {
    public static final String GROUP = "huskyteers";
    public static final boolean DISABLED = false;

    private HuskyOpModes() {
    }

    private static OpModeMeta metaForTeleOp(Class<? extends OpMode> cls, StartInfo startInfo, Class<? extends OpMode> autoCls) {
        return new OpModeMeta.Builder()
                .setName(cls.getSimpleName() + " - " + startInfo.toString())
                .setGroup(GROUP)
                .setFlavor(OpModeMeta.Flavor.TELEOP)
                .setTransitionTarget(autoCls.getSimpleName() + " - " + startInfo.toString())
                .build();
    }

    private static OpModeMeta metaForAuto(Class<? extends OpMode> cls, StartInfo startInfo) {
        return new OpModeMeta.Builder()
                .setName(cls.getSimpleName() + " - " + startInfo.toString())
                .setGroup(GROUP)
                .setFlavor(OpModeMeta.Flavor.AUTONOMOUS)
                .build();
    }

    @OpModeRegistrar
    public static void register(OpModeManager manager) {
        if (DISABLED) return;

        for (StartInfo.Position position : StartInfo.Position.values()) {
            for (StartInfo.Color color : StartInfo.Color.values()) {
                StartInfo startConfiguration = new StartInfo(color, position);
                manager.register(metaForTeleOp(HuskyTeleOp.class, startConfiguration, HuskyAuto.class), new HuskyTeleOp(startConfiguration));
                manager.register(metaForAuto(HuskyAuto.class, startConfiguration), new HuskyAuto(startConfiguration));
            }
        }
    }
}
