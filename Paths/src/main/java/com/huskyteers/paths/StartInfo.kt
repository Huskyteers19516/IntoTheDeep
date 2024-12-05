package com.huskyteers.paths

import com.acmerobotics.roadrunner.Pose2d


class StartInfo(var color: Color, var position: Position) {
    override fun toString(): String {
        return color.name + ", " + position.name
    }

    enum class Color {
        RED,
        BLUE
    }

    enum class Position(val pose2d: Pose2d) {
        CloseToBasket(
            Pose2d(
                TILE_LENGTH * -1.5,
                TILE_LENGTH * -3 + HEIGHT / 2,
                Math.toRadians(90.0)
            )
        ),
        FarFromBasket(
            Pose2d(
                TILE_LENGTH * 1.5,
                TILE_LENGTH * -3 + HEIGHT / 2,
                Math.toRadians(90.0)
            )
        )
    }

    companion object {

    }
}
