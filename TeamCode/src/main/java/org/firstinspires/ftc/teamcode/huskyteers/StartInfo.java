package org.firstinspires.ftc.teamcode.huskyteers;

import androidx.annotation.NonNull;

public final class StartInfo {
    Color color;
//    Position position;

    public StartInfo(Color color) {
        this.color = color;
//        this.position = position;
    }

    @NonNull
    @Override
    public String toString() {
        return color.name();
    }

    public enum Color {
        RED,
        BLUE
    }

    public enum Position {
        CloseToBasket,
        FarFromBasket
    }
}
