package org.firstinspires.ftc.teamcode.huskyteers.hardware;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class LinearSlide {
    private DcMotor motor;
    public LinearSlide(HardwareMap hardwareMap) {
        motor = hardwareMap.get(DcMotor.class, "slide");
    }

    public class MoveSlide implements Action {
        private boolean initialized = false;

        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            if (!initialized) {
                motor.setPower(0.1);
                initialized = true;
            }

            motor.setTargetPosition(2);

//            double vel = motor.velo
//                    ;
//            packet.put("shooterVelocity", vel);
//            return vel < 10_000.0;
            return false;
        }
    }
    public Action raiseUp() {
        return new MoveSlide();
    }
}
