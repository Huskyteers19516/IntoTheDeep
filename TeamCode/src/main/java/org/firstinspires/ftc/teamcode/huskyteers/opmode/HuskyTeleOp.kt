package org.firstinspires.ftc.teamcode.huskyteers.opmode

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.Action
import com.acmerobotics.roadrunner.InstantAction
import com.acmerobotics.roadrunner.Pose2d
import com.acmerobotics.roadrunner.SequentialAction
import com.acmerobotics.roadrunner.SleepAction
import com.huskyteers.paths.StartInfo
import org.firstinspires.ftc.teamcode.huskyteers.HuskyOpMode
import org.firstinspires.ftc.teamcode.huskyteers.utils.GamepadUtils
import java.util.concurrent.atomic.AtomicBoolean

class HuskyTeleOp(startInfo: StartInfo) : HuskyOpMode(startInfo) {
    private val dash: FtcDashboard = FtcDashboard.getInstance()
    private var runningActions: MutableList<Action> = ArrayList()

    enum class State {
        READY_TO_PICK_UP,
        EXTENDING,
        RETRACTED,
        RETRACTING,
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


        var state = State.RETRACTED

        gamepad1Utils.addRisingEdge("y") {
            if (state == State.READY_TO_PICK_UP) {
                state = State.RETRACTING
                runningActions.add(
                    SequentialAction(
                        horizontalExtender.retract(),
                        InstantAction { state = State.RETRACTED })
                )
            } else if (state == State.RETRACTED) {
                state = State.EXTENDING
                runningActions.add(
                    SequentialAction(
                        horizontalExtender.extend(),
                        InstantAction { state = State.READY_TO_PICK_UP })
                )
            }
        }

        gamepad1Utils.addRisingEdge("x") {
            if (state == State.READY_TO_PICK_UP) {
                state = State.GOING_TO_TOP
                runningActions.add(
                    SequentialAction(
                        InstantAction { bottomClaw.closeClaw() },
                        SleepAction(DELAY),
                        horizontalExtender.retract(),
                        InstantAction { bottomClaw.clawRotatorPosition = 180.0 },
                        SleepAction(DELAY),
                        InstantAction { topClaw.closeClaw() },
                        verticalExtender.extend(),
                        InstantAction { state = State.AT_TOP }
                    ))
            } else if (state == State.AT_TOP) {
                state = State.RELEASING
                runningActions.add(
                    SequentialAction(
                        InstantAction { topClaw.clawRotatorPosition = -180.0 },
                        SleepAction(DELAY),
                        InstantAction { topClaw.openClaw() },
                        InstantAction { topClaw.clawRotatorPosition = 180.0 },
                        SleepAction(DELAY),
                        verticalExtender.retract(),
                        InstantAction { state = State.RETRACTED }
                    ))
            }
        }
        while (opModeIsActive() && !isStopRequested) {
            val packet = TelemetryPacket()

            gamepad1Utils.processUpdates(gamepad1)
            gamepad2Utils.processUpdates(gamepad2)

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

    companion object {
        private const val DELAY = 1.0
    }
}
