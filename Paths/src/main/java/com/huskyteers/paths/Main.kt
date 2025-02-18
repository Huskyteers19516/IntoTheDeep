package com.huskyteers.paths

import com.acmerobotics.roadrunner.NullAction
import com.noahbres.meepmeep.MeepMeep
import com.noahbres.meepmeep.MeepMeep.Background
import com.noahbres.meepmeep.core.colorscheme.ColorScheme
import com.noahbres.meepmeep.core.colorscheme.scheme.ColorSchemeRedDark
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder
import com.noahbres.meepmeep.roadrunner.DriveTrainType
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity

var meepMeep: MeepMeep = MeepMeep(600)

fun createRobot(colorScheme: ColorScheme): RoadRunnerBotEntity {
    return DefaultBotBuilder(meepMeep) // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
        .setConstraints(
            60.0,
            60.0,
            Math.toRadians(180.0),
            Math.toRadians(30.0),
            WIDTH
        )
        .setDriveTrainType(DriveTrainType.MECANUM)
        .setDimensions(WIDTH, HEIGHT)
        .setColorScheme(colorScheme)
        .build()
}

fun main() {
    val backstageBot = createRobot(ColorSchemeRedDark())
    val delay = 1.0
    backstageBot.runAction(
        backstageBot.drive
            .actionBuilder(StartInfo.Position.CloseToBasket.pose2d)
            .run {
                closeToBasketToRightmostBrick(this)
            }
            .run { toBasket(this) }
            .run { basketToCenterBrick(this) }
            .run { toBasket(this) }
            .run {
                basketToLeftmostBrick(
                    this, { NullAction() })
            }
            .run {
                toBasket(this)
            }
            .run {
                toParking(this)
            }
            .build()
    )

    meepMeep.setBackground(Background.FIELD_INTO_THE_DEEP_JUICE_DARK)
        .setDarkMode(true)
        .setBackgroundAlpha(0.95f)
        .addEntity(backstageBot)
        .start()
}
