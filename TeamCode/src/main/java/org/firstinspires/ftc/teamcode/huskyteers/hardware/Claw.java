package org.firstinspires.ftc.teamcode.huskyteers.hardware;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Claw {

    public static final int OPEN_POSITION = 12;
    public static final int CLOSE_POSITION = 0;
    public static final double eps = 1e-5;
    public static final Servo.Direction FORWARD = Servo.Direction.FORWARD;
    public static final Servo.Direction REVERSE = Servo.Direction.REVERSE;
    private final Servo servo;

    public Claw(HardwareMap hardwareMap, String name) {
        servo = hardwareMap.get(Servo.class, name);
        servo.setDirection(FORWARD);
    }

    public boolean checkPosition(double s, double dest) {
        return Math.abs(s - dest) <= eps;
    }

    public Action openClaw() {
        return new Action(){
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
        return new Action(){
            private boolean initialized = false;
            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                if (!initialized) {
                    setPosition(CLOSE_POSITION);
                    initialized = true;
                }
                return checkPosition(getPosition(), CLOSE_POSITION);
            }
        };


    }

    public void setPosition(double pos){
        servo.setPosition(pos);
    }

    public double getPosition(){
        return servo.getPosition();
    }

}
