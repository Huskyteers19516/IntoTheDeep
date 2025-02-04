package org.firstinspires.ftc.teamcode.huskyteers.opmode

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.Pose2d
import com.huskyteers.paths.StartInfo
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.huskyteers.HuskyOpMode
import org.firstinspires.ftc.teamcode.huskyteers.utils.GamepadUtils
import java.util.concurrent.atomic.AtomicBoolean

@TeleOp
class DriveTesting : HuskyOpMode(StartInfo.empty()) {
    private val dash: FtcDashboard = FtcDashboard.getInstance()

    override fun runOpMode() {
        waitForStart()
        if (isStopRequested) return
        val gamepad1Utils = GamepadUtils()
        val gamepad2Utils = GamepadUtils()
        gamepad1Utils.addRisingEdge(
            "start"
        ) {
            drive.localizer.pose = Pose2d(
                drive.localizer.pose.position, 0.0
            )
        }

        val usingFieldCentric = AtomicBoolean(true)

        gamepad1Utils.addRisingEdge("a") {
            usingFieldCentric.set(!usingFieldCentric.get())
            gamepad1.rumble(200)
        }


        while (opModeIsActive() && !isStopRequested) {
            val packet = TelemetryPacket()

            gamepad1Utils.processUpdates(gamepad1)
            gamepad2Utils.processUpdates(gamepad2)

            val speed = (0.7 + 0.3 * gamepad1.left_trigger - 0.4 * gamepad1.right_trigger)

            if (usingFieldCentric.get()) {
                telemetry.addData("Drive Mode", "Field Centric")
                fieldCentricDriveRobot(
                    gamepad1.left_stick_y.toDouble(),
                    -gamepad1.left_stick_x.toDouble(),
                    -gamepad1.right_stick_x.toDouble(),
                    speed
                )
            } else {
                telemetry.addData("Drive Mode", "Tank")
                driveRobot(
                    gamepad1.left_stick_y.toDouble(),
                    -gamepad1.left_stick_x.toDouble(),
                    -gamepad1.right_stick_x.toDouble(),
                    speed
                )
            }


            telemetry.addData(
                "Left Stick Y", gamepad1.left_stick_y.toDouble(),
            )
            telemetry.addData(
                "Left Stick X", gamepad1.left_stick_x.toDouble(),
            )
            telemetry.addData(
                "Right Stick X", gamepad1.right_stick_x.toDouble(),
            )
            telemetry.addData(
                "IMU angle", drive.lazyImu.get().robotYawPitchRollAngles.yaw,
            )
            telemetry.addData(
                "Localizer angle", Math.toDegrees(drive.localizer.pose.heading.toDouble()),
            )

            telemetry.addData("Right Back Encoder Velocity", drive.rightBack.velocity)
            telemetry.addData("Right Back Encoder Position", drive.rightBack.currentPosition)

            telemetry.addData("Right Front Encoder Velocity", drive.rightFront.velocity)
            telemetry.addData("Right Front Encoder Position", drive.rightFront.currentPosition)

            telemetry.addData("Left Front Encoder Velocity", drive.leftFront.velocity)
            telemetry.addData("Left Front Encoder Position", drive.leftFront.currentPosition)

            telemetry.addData("Right Back Encoder Velocity", drive.rightBack.velocity)
            telemetry.addData("Right Back Encoder Position", drive.rightBack.currentPosition)


            dash.sendTelemetryPacket(packet)

            telemetry.update()
            sleep(20)
        }
    }

    companion object {
        private const val DELAY = 1.0
    }
}
