package com.huskyteers.paths

import com.acmerobotics.roadrunner.Pose2d
import com.acmerobotics.roadrunner.Vector2d
import com.noahbres.meepmeep.MeepMeep
import com.noahbres.meepmeep.MeepMeep.Background
import com.noahbres.meepmeep.core.colorscheme.ColorScheme
import com.noahbres.meepmeep.core.colorscheme.scheme.ColorSchemeBlueDark
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity

var meepMeep: MeepMeep = MeepMeep(600)

fun createRobot(colorScheme: ColorScheme): RoadRunnerBotEntity {
    return DefaultBotBuilder(meepMeep) // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
        .setConstraints(
            60.0,
            60.0,
            Math.toRadians(180.0),
            Math.toRadians(30.0),
            StartInfo.Companion.WIDTH
        )
        .setDimensions(StartInfo.Companion.WIDTH, StartInfo.Companion.HEIGHT)
        .setColorScheme(colorScheme)
        .build()
}

fun main(args: Array<String>) {
    val backstageBot = createRobot(ColorSchemeBlueDark())
    backstageBot.runAction(
        backstageBot.drive
            .actionBuilder(Pose2d(0.0, 0.0, Math.toRadians(90.0)))
            .splineToLinearHeading(Pose2d(48.0, 48.0, 0.0), Math.PI / 2)
            .splineTo(Vector2d(30.0, 60.0), Math.toRadians(180.0))
            .build()
    )

    meepMeep.setBackground(Background.FIELD_INTO_THE_DEEP_JUICE_DARK)
        .setDarkMode(true)
        .setBackgroundAlpha(0.95f)
        .addEntity(backstageBot)
        .start()
}
