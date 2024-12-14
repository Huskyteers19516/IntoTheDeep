package org.firstinspires.ftc.teamcode.huskyteers.opmode

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.Action
import com.acmerobotics.roadrunner.Pose2d
import com.huskyteers.paths.StartInfo
import org.firstinspires.ftc.teamcode.huskyteers.HuskyOpMode
import org.firstinspires.ftc.teamcode.huskyteers.hardware.ArmSlide
import org.firstinspires.ftc.teamcode.huskyteers.hardware.VerticalExtender
import org.firstinspires.ftc.teamcode.huskyteers.utils.GamepadUtils
import java.util.concurrent.atomic.AtomicBoolean

class HuskyTeleOp(startInfo: StartInfo) : HuskyOpMode(startInfo) {
    private val dash: FtcDashboard = FtcDashboard.getInstance()
    private var runningActions: List<Action> = ArrayList()

    override fun runOpMode() {
        waitForStart()
        if (isStopRequested) return
        val gamepad1Utils = GamepadUtils()
        val gamepad2Utils = GamepadUtils()
        gamepad1Utils.addRisingEdge(
            "start"
        ) {
            drive.pose = Pose2d(
                drive.pose.position, 0.0
            )
        }

        val usingFieldCentric = AtomicBoolean(false)

        gamepad1Utils.addRisingEdge(
            "right_bumper"
        ) { armSlide.setPosition(ArmSlide.EXTEND_POSITION) }
        gamepad1Utils.addRisingEdge(
            "left_bumper"
        ) { armSlide.setPosition(ArmSlide.RETRACT_POSITION) }

        gamepad1Utils.addRisingEdge(
            "b"
        ) {
            sampleRotation.ifPresent { rotation: Double ->
                claw.rotateClaw(
                    rotation
                )
            }
        }

        gamepad2Utils.addRisingEdge(
            "x"
        ) { claw.openClaw() }
        gamepad2Utils.addRisingEdge(
            "y"
        ) { claw.closeClaw() }

        gamepad1Utils.addRisingEdge("a") {
            usingFieldCentric.set(!usingFieldCentric.get())
            gamepad1.rumble(200)
        }
        gamepad1Utils.addRisingEdge(
            "dpad_up"
        ) {
            visionPortal.resumeStreaming()
        }
        gamepad2Utils.addRisingEdge(
            "dpad_up"
        ) {
            horizontalExtender.extend();
        }
        gamepad2Utils.addRisingEdge(
            "dpad_down"
        ) {
            horizontalExtender.retract()
        }
        gamepad2Utils.addRisingEdge("b") {
            if (verticalExtender.state == VerticalExtender.State.EXTENDED) {
                verticalExtender.retract()
            } else {
                verticalExtender.extend()
            }
        }
        gamepad2Utils.addRisingEdge(
            "left_bumper"
        ) {
            basket.drop()
        }
        gamepad2Utils.addRisingEdge(
            "right_bumper"
        ) {
            basket.raise()
        }

        while (opModeIsActive() && !isStopRequested) {
            val packet = TelemetryPacket()

            gamepad1Utils.processUpdates(gamepad1)
            gamepad2Utils.processUpdates(gamepad2)
            val clawSpeed = gamepad2.right_trigger * 0.05 - gamepad2.left_trigger * 0.05
            claw.rotateClaw(clawSpeed)

            val speed = (0.7 + 0.3 * gamepad1.left_trigger - 0.4 * gamepad1.right_trigger)
            if (gamepad1.a) {
                usingFieldCentric.set(!usingFieldCentric.get())
            }
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

            // update running actions
            val newActions: MutableList<Action> = ArrayList()
            for (action in runningActions) {
                action.preview(packet.fieldOverlay())
                if (action.run(packet)) {
                    newActions.add(action)
                }
            }
            runningActions = newActions

            dash.sendTelemetryPacket(packet)
            telemetry.update()
            sleep(20)
        }
        visionPortal.close()
    }
}
