package org.firstinspires.ftc.teamcode.huskyteers.utils;

import androidx.annotation.NonNull;

import com.acmerobotics.roadrunner.Pose2d;

public final class StartInfo {
    public static final double TILE_LENGTH = 24;
    public final static double HEIGHT = 17;
    public final static double WIDTH = 17;

    public Color color;
    public Position position;

    public StartInfo(Color color, Position position) {
        this.color = color;
        this.position = position;
    }

    @NonNull
    @Override
    public String toString() {
        return color.name() + ", " + position.name();
    }

    public enum Color {
        RED,
        BLUE
    }

    public enum Position {
        CloseToBasket(new Pose2d(TILE_LENGTH * 1.5, TILE_LENGTH * -3 + HEIGHT, Math.toRadians(90))),
        FarFromBasket(new Pose2d(TILE_LENGTH * -1.5, TILE_LENGTH * -3 + HEIGHT, Math.toRadians(90)));

        public final Pose2d pose2d;

        Position(Pose2d pose2d) {
            this.pose2d = pose2d;
        }
    }
}
