package com.huskyteers.paths

import com.acmerobotics.roadrunner.Rotation2d
import com.acmerobotics.roadrunner.Vector2d

const val HEIGHT: Double = 17.0
const val WIDTH: Double = 17.0

fun clawOffset(angle: Rotation2d): Vector2d {
    return angle * Vector2d(10.0, 0.0)
}