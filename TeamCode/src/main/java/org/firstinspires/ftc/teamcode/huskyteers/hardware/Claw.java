package org.firstinspires.ftc.teamcode.huskyteers.hardware;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Claw {
    private static final int OPEN_POSITION = 100;
    private static final int CLOSE_POSITION = 0;
    private Servo servo;
    public Claw(HardwareMap hardwareMap) {
        servo = hardwareMap.get(Servo.class, "claw");
    }

    public void setPosition(int position) {
        servo.setPosition(position);
    }

    public Action openClaw() {
        return new Action() {
            private boolean initialized = false;

            @Override
            public boolean run(TelemetryPacket telemetryPacket) {
                if (!initialized) {
                    setPosition(OPEN_POSITION);
                    initialized = true;
                }
                return servo.getPosition() < OPEN_POSITION - 5;
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
            public boolean run(TelemetryPacket telemetryPacket) {
                if (!initialized) {
                    setPosition(CLOSE_POSITION);
                    initialized = true;
                }
                if (servo.getPosition() == previousPosition) {
                    servo.setPosition(previousPosition);
                    return false;
                }
                if (interval%timing == 0) {
                    previousPosition = servo.getPosition();
                }
                interval++;
                return servo.getPosition() > CLOSE_POSITION + 5;
            }
        };
    }
}
