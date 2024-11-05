package org.firstinspires.ftc.teamcode.huskyteers.hardware;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class ArmSlide {
    public static final int EXTEND_POSITION = 100;
    public static final int RETRACT_POSITION = 0;
    final private DcMotor motor;

    public ArmSlide(HardwareMap hardwareMap) {
        motor = hardwareMap.get(DcMotor.class, "armSlide");
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    public void setPosition(int position) {
        motor.setTargetPosition(position);
    }

    public Action extendArm() {
        return new Action() {
            private boolean initialized = false;

            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                if (!initialized) {
                    setPosition(EXTEND_POSITION);
                    initialized = true;
                }
                return motor.isBusy();
            }
        };
    }

    public Action retractArm() {
        return new Action() {
            private boolean initialized = false;

            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                if (!initialized) {
                    setPosition(RETRACT_POSITION);
                    initialized = true;
                }
                return motor.isBusy();
            }
        };
    }
}
