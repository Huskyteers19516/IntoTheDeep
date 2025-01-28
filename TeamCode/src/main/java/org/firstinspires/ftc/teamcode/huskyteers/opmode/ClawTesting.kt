package org.firstinspires.ftc.teamcode.huskyteers.opmode

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.InstantAction
import com.acmerobotics.roadrunner.NullAction
import com.acmerobotics.roadrunner.Pose2d
import com.acmerobotics.roadrunner.SequentialAction
import com.acmerobotics.roadrunner.SleepAction
import com.huskyteers.paths.StartInfo
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.huskyteers.HuskyOpMode
import org.firstinspires.ftc.teamcode.huskyteers.utils.FailoverAction
import org.firstinspires.ftc.teamcode.huskyteers.utils.GamepadUtils
import java.util.concurrent.atomic.AtomicBoolean

@TeleOp
class ClawTesting : HuskyOpMode(StartInfo(StartInfo.Color.RED, StartInfo.Position.CloseToBasket)) {
    private val dash: FtcDashboard = FtcDashboard.getInstance()

    enum class State {
        READY_TO_PICK_UP,
        GOING_TO_TOP,
        AT_TOP,
        RELEASING
    }

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

        var state = State.READY_TO_PICK_UP
        var clawAction: FailoverAction? = null

        gamepad1Utils.addRisingEdge("x") {
            if (state == State.READY_TO_PICK_UP) {
                state = State.GOING_TO_TOP
                clawAction = FailoverAction(
                    SequentialAction(
                        InstantAction { topClaw.closeClaw() },
                        SleepAction(DELAY),
                        InstantAction { topClaw.rotateUp() },
                        verticalExtender.extend(),
                        InstantAction { state = State.AT_TOP }
                    ),
                    NullAction()
                )
            } else if (state == State.AT_TOP) {
                state = State.RELEASING
                clawAction = FailoverAction(
                    SequentialAction(
                        InstantAction { topClaw.rotateUp() },
                        SleepAction(DELAY),
                        InstantAction { topClaw.openClaw() },
                        SleepAction(DELAY),
                        InstantAction { topClaw.rotateDown() },
                        verticalExtender.retract(),
                        InstantAction { state = State.READY_TO_PICK_UP }
                    ),
                    NullAction()
                )
            }
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

            clawAction = if (clawAction?.run(packet) == true) clawAction else null

            dash.sendTelemetryPacket(packet)
            telemetry.update()
            sleep(20)
        }
    }

    companion object {
        private const val DELAY = 1.0
    }
}
