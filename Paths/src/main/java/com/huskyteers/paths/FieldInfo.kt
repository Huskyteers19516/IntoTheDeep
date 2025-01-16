package com.huskyteers.paths

import com.acmerobotics.roadrunner.Rotation2d
import com.acmerobotics.roadrunner.Vector2d

const val TILE_LENGTH: Double = 24.0
const val BRICK_LENGTH: Double = 3.5
const val BRICK_WIDTH: Double = 1.5
const val BRICK_DISTANCE: Double = 10.0
val BASKET_OFFSET: Vector2d = Vector2d(5.392, 5.392)
val BASKET_ANGLE = Rotation2d.fromDouble(Math.toRadians(225.0))