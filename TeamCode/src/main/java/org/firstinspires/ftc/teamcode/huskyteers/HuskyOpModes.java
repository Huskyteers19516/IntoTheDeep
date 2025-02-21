package org.firstinspires.ftc.teamcode.huskyteers;

import com.huskyteers.paths.StartInfo;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpModeManager;
import com.qualcomm.robotcore.eventloop.opmode.OpModeRegistrar;
import org.firstinspires.ftc.robotcore.internal.opmode.OpModeMeta;
import org.firstinspires.ftc.teamcode.huskyteers.opmode.HuskyTeleOp;

public final class HuskyOpModes {

    public static final String GROUP = "huskyteers";
    public static final boolean DISABLED = true;

    private static OpModeMeta metaForTeleOp(Class<? extends OpMode> cls, StartInfo startInfo) {
        return new OpModeMeta.Builder()
                .setName(cls.getSimpleName() + " - " + startInfo.toString())
                .setGroup(GROUP)
                .setFlavor(OpModeMeta.Flavor.TELEOP)
                .build();
    }

    private static OpModeMeta metaForAuto(Class<? extends OpMode> cls, StartInfo startInfo, Class<? extends OpMode> teleOpCls) {
        return new OpModeMeta.Builder()
                .setName(cls.getSimpleName() + " - " + startInfo.toString())
                .setGroup(GROUP)
                .setFlavor(OpModeMeta.Flavor.AUTONOMOUS)
                .setTransitionTarget(teleOpCls.getSimpleName() + " - " + startInfo.toString())
                .build();
    }

    @OpModeRegistrar
    public static void register(OpModeManager manager) {
        System.out.println("registering op modes");
        if (DISABLED) return;

        for (StartInfo.Color color : StartInfo.Color.getEntries()) {
            System.out.println("registering op modes for " + color.toString());

            StartInfo teleOpStartConfiguration = new StartInfo(color, StartInfo.Position.None);
            manager.register(
                    metaForTeleOp(HuskyTeleOp.class, teleOpStartConfiguration),
                    new HuskyTeleOp(teleOpStartConfiguration)
            );

//            for (StartInfo.Position position : StartInfo.Position.getEntries()) {
//                StartInfo autoStartConfiguration = new StartInfo(color, position);
//                manager.register(
//                        metaForAuto(HuskyAuto.class, autoStartConfiguration, HuskyTeleOp.class),
//                        new HuskyAuto(autoStartConfiguration)
//                );
//            }
        }
//        FtcDashboard.getInstance().withConfigRoot(configRoot -> {
//            for (Class<?> c : Arrays.asList(
//                    HuskyTeleOp.class
//            )) {
//                configRoot.putVariable(c.getSimpleName(), ReflectionConfig.createVariableFromClass(c));
//            }
//        });
    }
}