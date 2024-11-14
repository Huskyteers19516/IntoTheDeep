package org.firstinspires.ftc.teamcode.huskyteers.hardware;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Claw {
    private static final int OPEN_POSITION = 100;
    private static final int CLOSE_POSITION = 0;
    public static final double eps = 1e-5;
    private final Servo servo;

    public Claw(HardwareMap hardwareMap) {
        servo = hardwareMap.get(Servo.class, "claw");
    }

    public void setPosition(int position) {
        servo.setPosition(position);
    }

    public boolean checkPosition(double s, double dest) {
        return Math.abs(s - dest) <= eps;
    }

    public Action openClaw() {
        return new Action() {
            private boolean initialized = false;

            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                if (!initialized) {
                    setPosition(OPEN_POSITION);
                    initialized = true;
                }

                return checkPosition(getPosition(), OPEN_POSITION);
            }

        };
    }


    public Action closeClaw() {
        return new Action() {
            private boolean initialized = false;
            private double previousPosition;
            private int timing = 10;
            private int interval = 0;

            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                if (!initialized) {
                    setPosition(CLOSE_POSITION);
                    initialized = true;
                }
                if (servo.getPosition() == previousPosition) {
                    setPosition(previousPosition);
                    return false;
                }
                if (interval % timing == 0) {
                    previousPosition = getPosition();
                }
                interval++;
                return checkPosition(getPosition(), CLOSE_POSITION);
            }
        };
    }

    public void setPosition(double pos) {
        servo.setPosition(pos);
    }

    public double getPosition() {
        return servo.getPosition();
    }
}
