package com.huskyteers.paths;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
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
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(30), WIDTH)
                .setDimensions(WIDTH, HEIGHT)
                .setColorScheme(colorScheme)
                .build();
    }

    public static void main(String[] args) {
        RoadRunnerBotEntity backstageBot = createRobot(new ColorSchemeBlueDark());
        backstageBot.runAction(
                backstageBot.getDrive()
                        .actionBuilder(new Pose2d(0, 0, Math.toRadians(90)))
                        .splineToLinearHeading(new Pose2d(48, 48, 0), Math.PI / 2)
                        .splineTo(new Vector2d(30, 60), Math.toRadians(180))
                        .build()
        );

        meepMeep.setBackground(MeepMeep.Background.FIELD_INTO_THE_DEEP_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(backstageBot)
                .start();

    }
}