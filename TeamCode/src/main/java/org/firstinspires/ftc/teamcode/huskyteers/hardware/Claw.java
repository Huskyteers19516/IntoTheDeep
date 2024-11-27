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
     * @param angle Degrees
     */
    public void rotateClaw(double angle) {
         /*
         Rotates claw to match the angle or it's 180 degree variant, whichever is closer
         Since it is currently a servo, passing from 0 to 1 or 1 to 0 requires going through the
         entire range of positions.
         https://www.tldraw.com/s/v2_c_qJ9m3Ck499qom_FbU8zcj?d=v91.496.1814.914.page
          */
        double target1 = angle / 360;
        double target2 = target1 + (target1 > 0.5 ? -0.5 : 0.5);
        double currentPosition = getClawRotatorPosition();
        if (Math.abs(target1 - currentPosition) < Math.abs(target2 - currentPosition)) {
            setClawRotatorPosition(target1);
        } else {
            setClawRotatorPosition(target2);
        }
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
