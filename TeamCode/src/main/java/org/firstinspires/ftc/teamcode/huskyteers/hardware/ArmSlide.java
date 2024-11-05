package org.firstinspires.ftc.teamcode.huskyteers.hardware;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class ArmSlide {
    final int EXTEND_POSITION = 100;
    final int RETRACT_POSITION = 0;
    private DcMotor motor;
    public ArmSlide(HardwareMap hardwareMap) {
        motor = hardwareMap.get(DcMotor.class, "armSlide");
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    public class ExtendArm implements Action {
        public ExtendArm() {
            motor.setTargetPosition(EXTEND_POSITION);
        }
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            return motor.isBusy();
        }
    }

    public Action extendArm() {
        return new ExtendArm();
    }

    public class RetractArm implements Action {
        public RetractArm() {
            motor.setTargetPosition(RETRACT_POSITION);
        }
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            return motor.isBusy();
        }
    }
    public Action retractArm() {
        return new RetractArm();
    }

}
