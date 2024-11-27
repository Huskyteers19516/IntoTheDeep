package org.firstinspires.ftc.teamcode.huskyteers;

public final class StartInfo {
    Color color;
    Position position;

    public StartInfo(Color color, Position position) {
        this.color = color;
        this.position = position;
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
