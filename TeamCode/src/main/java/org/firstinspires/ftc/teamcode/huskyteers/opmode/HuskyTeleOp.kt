package org.firstinspires.ftc.teamcode.huskyteers.opmode

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.Action
import com.acmerobotics.roadrunner.Pose2d
import com.acmerobotics.roadrunner.SequentialAction
import com.acmerobotics.roadrunner.ftc.runBlocking
import com.huskyteers.paths.StartInfo
import org.firstinspires.ftc.teamcode.huskyteers.HuskyOpMode
import org.firstinspires.ftc.teamcode.huskyteers.utils.GamepadUtils
import java.util.concurrent.atomic.AtomicBoolean

class HuskyTeleOp(startInfo: StartInfo) : HuskyOpMode(startInfo) {
    private val dash: FtcDashboard = FtcDashboard.getInstance()

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
        var clawAction: Action? = null

        gamepad1Utils.addRisingEdge("y") {
            if (state == State.READY_TO_PICK_UP || state == State.EXTENDING) {
                state = State.RETRACTED
                bottomClaw.rotateUp()
                bottomClaw.openClaw()
                horizontalExtender.motor.targetPosition = 0

//                clawAction =
//                    SequentialAction(
//                        InstantAction { bottomClaw.openClaw() },
//                        InstantAction { bottomClaw.rotateUp() },
//                        InstantAction {
//                            horizontalExtender.power = 0.7
//                            horizontalExtender.runMode = DcMotor.RunMode.RUN_TO_POSITION
//                        },
//                        horizontalExtender.retract(),
//                        InstantAction { state = State.RETRACTED }
//                    )
            } else if (state == State.RETRACTED || state == State.RETRACTING) {
                state = State.READY_TO_PICK_UP
                bottomClaw.openClaw()
                bottomClaw.rotateDown()
                horizontalExtender.position = 1500
            }
        }

        gamepad1Utils.addRisingEdge("x") {
            if (state == State.READY_TO_PICK_UP) {
                state = State.AT_TOP
                bottomClaw.closeClaw()
                sleep(2000)
                bottomClaw.rotateUp()
                topClaw.openClaw()
                topClaw.rotateDown()
                horizontalExtender.position = 0
                topClaw.closeClaw()
                bottomClaw.openClaw()
                sleep(2000)
                topClaw.rotateUp()
                verticalExtender.position = -3000
//                clawAction = SequentialAction(
//                    InstantAction {
//                        horizontalExtender.power = 0.7
//                        horizontalExtender.runMode = DcMotor.RunMode.RUN_TO_POSITION
//                    },
//                    InstantAction { bottomClaw.closeClaw() },
//                    SleepAction(DELAY),
//                    InstantAction { bottomClaw.rotateUp() },
//                    horizontalExtender.retract(),
//                    InstantAction { topClaw.closeClaw() },
//                    InstantAction { bottomClaw.openClaw() },
//                    SleepAction(DELAY),
//                    InstantAction { topClaw.rotateUp() },
//                    verticalExtender.extend(),
//                    InstantAction { state = State.AT_TOP }
//                )
            } else if (state == State.AT_TOP) {
                state = State.RETRACTED
                topClaw.rotateUp()
                sleep(2000)
                topClaw.openClaw()
                sleep(2000)
                verticalExtender.position = 0
                topClaw.rotateDown()
//                clawAction = SequentialAction(
//                    InstantAction { topClaw.rotateUp() },
//                    SleepAction(DELAY / 2),
//                    InstantAction { topClaw.openClaw() },
//                    SleepAction(DELAY),
//                    InstantAction { topClaw.rotateDown() },
//                    verticalExtender.retract(),
//                    InstantAction { state = State.RETRACTED }
//                )
            }
        }
        gamepad2Utils.addRisingEdge("a") {
            runBlocking(SequentialAction(lifter.lift()))
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

            if (state == State.READY_TO_PICK_UP) {
//                if (gamepad1.dpad_up) {
//                    if (horizontalExtender.position < HorizontalExtender.MAX_EXTENDED) {
//                        horizontalExtender.power = 1.0
//                    }
//                } else if (gamepad1.dpad_down) {
//                    horizontalExtender.power = -1.0
//                } else {
//                    horizontalExtender.power = 0.0
//                }
            }
            lifter.servo.position += (gamepad2.left_stick_x.toDouble() / 0.3).coerceAtLeast(0.0)
                .coerceAtMost(1.0)


            clawAction = if (clawAction?.run(packet) == true) clawAction else null
            telemetry.addData("Claw State", state.name)
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
                "Bottom Claw Rotator", bottomClaw.clawRotatorPosition,
            )
            telemetry.addData("Horizontal Extender Encoder", horizontalExtender.position)
            telemetry.addData("Vertical Extender Encoder", verticalExtender.position)



            dash.sendTelemetryPacket(packet)

            telemetry.update()
            sleep(20)
        }
    }

    companion object {
        private const val DELAY = 1.0
    }
}
