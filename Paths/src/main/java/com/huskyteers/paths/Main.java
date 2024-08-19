package com.huskyteers.paths;

import com.acmerobotics.roadrunner.Pose2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.core.colorscheme.ColorScheme;
import com.noahbres.meepmeep.core.colorscheme.scheme.ColorSchemeBlueDark;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class Main {
    public static final int WIDTH = 18;
    public static final int HEIGHT = 18;
    public static MeepMeep meepMeep = new MeepMeep(600);

    public static RoadRunnerBotEntity createRobot(ColorScheme colorScheme) {
        return new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), WIDTH)
                .setDimensions(WIDTH, HEIGHT)
                .setColorScheme(colorScheme)
                .build();
    }

    public static void main(String[] args) {
        RoadRunnerBotEntity backstageBot = createRobot(new ColorSchemeBlueDark());
        backstageBot.runAction(
                Paths.examplePath(
                        backstageBot.getDrive().actionBuilder(new Pose2d(0, 0, 0))
                ).build());

        meepMeep.setBackground(MeepMeep.Background.FIELD_CENTERSTAGE_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(backstageBot)
                .start();

    }
}