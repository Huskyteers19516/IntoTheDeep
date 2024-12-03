package com.huskyteers.paths

import com.acmerobotics.roadrunner.TrajectoryActionBuilder
import com.acmerobotics.roadrunner.Vector2d

fun examplePath(actionBuilder: TrajectoryActionBuilder): TrajectoryActionBuilder {
    return actionBuilder.strafeTo(Vector2d(10.0, 10.0))
}
