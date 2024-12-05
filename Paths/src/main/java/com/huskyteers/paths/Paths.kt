package com.huskyteers.paths

import com.acmerobotics.roadrunner.TrajectoryActionBuilder
import com.acmerobotics.roadrunner.Vector2d

fun closeToBasketToRightmostBrick(actionBuilder: TrajectoryActionBuilder): TrajectoryActionBuilder {
    return actionBuilder.splineTo(
        Vector2d(
            TILE_LENGTH * -2 - BRICK_WIDTH / 2,
            TILE_LENGTH * -1 - BRICK_LENGTH / 2
        ) - CLAW_OFFSET,
        Math.toRadians(90.0)
    )
}

