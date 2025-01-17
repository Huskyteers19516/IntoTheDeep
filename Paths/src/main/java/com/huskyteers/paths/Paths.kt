package com.huskyteers.paths

import com.acmerobotics.roadrunner.Pose2d
import com.acmerobotics.roadrunner.Rotation2d
import com.acmerobotics.roadrunner.TrajectoryActionBuilder
import com.acmerobotics.roadrunner.Vector2d

fun closeToBasketToRightmostBrick(actionBuilder: TrajectoryActionBuilder): TrajectoryActionBuilder {
    val angle = Rotation2d.exp(Math.toRadians(90.0))
    return actionBuilder.splineToSplineHeading(
        Pose2d(
            Vector2d(
                TILE_LENGTH * -2,
                TILE_LENGTH * -1
            ) - Vector2d(BRICK_WIDTH, BRICK_LENGTH) / 2.0 - clawOffset(angle),
            angle
        ),
        angle
    )
}

fun basketToCenterBrick(actionBuilder: TrajectoryActionBuilder): TrajectoryActionBuilder {
    val angle = Rotation2d.exp(Math.toRadians(90.0))
    return actionBuilder
        .setTangent(BASKET_ANGLE + Math.PI)
        .splineToSplineHeading(
            Pose2d(
                Vector2d(
                    TILE_LENGTH * -2 - BRICK_DISTANCE,
                    TILE_LENGTH * -1
                ) - Vector2d(BRICK_WIDTH, BRICK_LENGTH) / 2.0 - clawOffset(angle),
                angle
            ),
            angle
        )
}

fun basketToLeftmostBrick(actionBuilder: TrajectoryActionBuilder): TrajectoryActionBuilder {
    val angle = Rotation2d.exp(Math.toRadians(90.0))
    println(Math.toDegrees((BASKET_ANGLE + Math.PI).log()))
    return actionBuilder
        .setTangent(BASKET_ANGLE + Math.PI)
        .splineToSplineHeading(
            Pose2d(
                Vector2d(
                    TILE_LENGTH * -2.0 - BRICK_DISTANCE * 2,
                    TILE_LENGTH * -1
                ) - Vector2d(BRICK_WIDTH, BRICK_LENGTH) / 2.0 - clawOffset(angle),
                angle
            ),
            angle
        )
}

fun toBasket(actionBuilder: TrajectoryActionBuilder): TrajectoryActionBuilder {
    return actionBuilder
        .setTangent(Math.toRadians(270.0))
        .splineToSplineHeading(
            Pose2d(
                Vector2d(
                    TILE_LENGTH * -3,
                    TILE_LENGTH * -3
                ) + BASKET_OFFSET - clawOffset(BASKET_ANGLE),
                BASKET_ANGLE
            ),
            BASKET_ANGLE
        )
}

fun toParking(actionBuilder: TrajectoryActionBuilder): TrajectoryActionBuilder {
    val angleAboveWall = 20.0
    return actionBuilder
        .setTangent(Math.toRadians(angleAboveWall))
        .splineToSplineHeading(
            Pose2d(
                Vector2d(
                    TILE_LENGTH * 1.5,
                    TILE_LENGTH * -3 + HEIGHT / 2
                ),
                Math.toRadians(90.0),
            ),
            Math.toRadians(-angleAboveWall)
        )
}
