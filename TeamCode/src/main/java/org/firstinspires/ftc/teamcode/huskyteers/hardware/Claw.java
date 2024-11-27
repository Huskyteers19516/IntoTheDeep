package org.firstinspires.ftc.teamcode.huskyteers.hardware;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Claw {
    public static final double eps = 1e-5;
    private static final double OPEN_POSITION = 1.0;
    private static final double CLOSE_POSITION = 0;
    private final Servo clawOpenerServo;
    private final Servo clawRotatorServo;

    public Claw(HardwareMap hardwareMap) {
        clawOpenerServo = hardwareMap.get(Servo.class, "clawOpener");
        clawRotatorServo = hardwareMap.get(Servo.class, "clawRotator");
    }

    /**
     * @param angle Angle of sample in degrees counterclockwise, with 0 being to the right
     *              (like a unit circle)
     */
    public void rotateClaw(double angle) {
        setClawRotatorPosition(angle / 180);
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
                    setClawOpenerPosition(OPEN_POSITION);
                    initialized = true;
                }

                return checkPosition(getClawOpenerPosition(), OPEN_POSITION);
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
                    setClawOpenerPosition(CLOSE_POSITION);
                    initialized = true;
                }
                if (clawOpenerServo.getPosition() == previousPosition) {
                    setClawOpenerPosition(previousPosition);
                    return false;
                }
                if (interval % timing == 0) {
                    previousPosition = getClawOpenerPosition();
                }
                interval++;
                return checkPosition(getClawOpenerPosition(), CLOSE_POSITION);
            }
        };
    }

    public double getClawOpenerPosition() {
        return clawOpenerServo.getPosition();
    }

    public void setClawOpenerPosition(double pos) {
        clawOpenerServo.setPosition(pos);
    }

    public double getClawRotatorPosition() {
        return clawRotatorServo.getPosition();
    }

    public void setClawRotatorPosition(double pos) {
        clawRotatorServo.setPosition(pos);
    }
}
