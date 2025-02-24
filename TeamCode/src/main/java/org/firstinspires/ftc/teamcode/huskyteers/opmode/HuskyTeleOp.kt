package org.firstinspires.ftc.teamcode.huskyteers.opmode

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.*
import com.huskyteers.paths.StartInfo
import org.firstinspires.ftc.teamcode.huskyteers.HuskyOpMode
import org.firstinspires.ftc.teamcode.huskyteers.hardware.IntakeClaw
import org.firstinspires.ftc.teamcode.huskyteers.hardware.OuttakeClaw
import org.firstinspires.ftc.teamcode.huskyteers.utils.GamepadUtils
import kotlin.math.max


class HuskyTeleOp(startInfo: StartInfo) : HuskyOpMode(startInfo) {
    private val dash: FtcDashboard = FtcDashboard.getInstance()

    enum class State {
        AT_BOTTOM,
        MOVING_CLAW_DOWN,
        MOVING_CLAW_UP,
        CLAW_DOWN,
        PICKING_UP,
        PICKED_UP,
        GOING_TO_TOP,
        AT_TOP,
        DROPPING,
    }


    override fun runOpMode() {
        initializeEverything()
        telemetry = MultipleTelemetry(telemetry, FtcDashboard.getInstance().telemetry)

        waitForStart()
        if (isStopRequested) return
        val gamepad1Utils = GamepadUtils()
        val gamepad2Utils = GamepadUtils()

        var outtakeSlideHoldPosition = outtakeSlide.position

        //#region Driving Controls
        gamepad1Utils.addRisingEdge(
            "start"
        ) {
            drive.localizer.pose = Pose2d(
                drive.localizer.pose.position, 0.0
            )
        }

        var usingFieldCentric = true

        gamepad1Utils.addRisingEdge("a") {
            usingFieldCentric = !usingFieldCentric
            gamepad1.rumble(200)
        }
        //#endregion

        //#region Intake and Outtake Controls

        var currentAction: Action? = null

        var state = State.AT_BOTTOM

        gamepad1Utils.addRisingEdge("dpad_down") {
            // Move the intake claw down
            if (arrayOf(State.AT_BOTTOM, State.MOVING_CLAW_UP).contains(state)) {
                state = State.MOVING_CLAW_DOWN
                currentAction = SequentialAction(
                    InstantAction {
                        intakeClaw.rotateDown()
                    },
                    SleepAction(IntakeClaw.ROTATOR_TIME),
                    InstantAction {
                        state = State.CLAW_DOWN
                    }
                )
            }
        }

        var outtakeSlideFast = false

        gamepad1Utils.addRisingEdge("b") {
            outtakeSlideFast = !outtakeSlideFast
        }


        gamepad1Utils.addRisingEdge("dpad_up") {
            // Move the intake claw up
            if (arrayOf(State.CLAW_DOWN, State.MOVING_CLAW_DOWN).contains(state)) {
                currentAction = SequentialAction(
                    InstantAction {
                        intakeClaw.rotateUp()
                    }, SleepAction(IntakeClaw.ROTATOR_TIME), InstantAction {
                        state = State.AT_BOTTOM
                    }
                )
            }
        }

        gamepad1Utils.addRisingEdge("y") {
            if (state == State.PICKED_UP) {
                state = State.GOING_TO_TOP
                currentAction = SequentialAction(
                    InstantAction {
                        outtakeClaw.close()
                    },
                    SleepAction(OuttakeClaw.GRAB_TIME),
                    InstantAction {
                        intakeClaw.open()
                    },
                    SleepAction(IntakeClaw.GRAB_TIME),
                    outtakeSlide.extendToLowBasket(),
                    InstantAction { outtakeClaw.rotateUp() },
                    SleepAction(OuttakeClaw.ROTATOR_TIME),
                    InstantAction {
                        state = State.AT_TOP
                    }
                )
            } else if (state == State.AT_TOP) {
                state = State.DROPPING
                currentAction = SequentialAction(
                    InstantAction {
                        outtakeClaw.open()
                    },
                    SleepAction(OuttakeClaw.GRAB_TIME),
                    outtakeSlide.extendToLowBasket(),
                    InstantAction {
                        outtakeClaw.rotateDown()
                    },
                    SleepAction(OuttakeClaw.ROTATOR_TIME),
                    outtakeSlide.retract(),
                    InstantAction {
                        state = State.AT_BOTTOM
                    }
                )
            }
        }

        gamepad1Utils.addRisingEdge("x") {
            if (State.CLAW_DOWN == state) {
                // Grab sample from submersible and retract into robot
                state = State.PICKING_UP
                currentAction = SequentialAction(
                    InstantAction { intakeClaw.close() },
                    SleepAction(IntakeClaw.GRAB_TIME),
                    InstantAction {
                        intakeClaw.rotateUp()
                        outtakeClaw.rotateDown()
                    },
                    InstantAction {
                        state = State.PICKED_UP
                    }
                )
            } else if (state == State.PICKED_UP) {
                // Drop sample from claw
                state = State.MOVING_CLAW_DOWN
                currentAction = SequentialAction(
                    InstantAction { intakeClaw.rotateDown() },
                    SleepAction(IntakeClaw.ROTATOR_TIME),
                    InstantAction {
                        intakeClaw.open()
                        outtakeClaw.open()
                    },
                    SleepAction(max(OuttakeClaw.GRAB_TIME, IntakeClaw.GRAB_TIME)),
                    InstantAction {
                        state = State.CLAW_DOWN
                    }
                )
            }
        }

        //#endregion


        while (opModeIsActive() && !isStopRequested) {
            val packet = TelemetryPacket()

            gamepad1Utils.processUpdates(gamepad1)
            gamepad2Utils.processUpdates(gamepad2)

            outtakeSlide.checkBottom()

            //#region Driving
            val speed =
                (DEFAULT_SPEED + SPEED_BOOST * gamepad1.left_trigger - SPEED_REDUCTION * gamepad1.right_trigger)

            if (usingFieldCentric) {
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

            //#endregion

            //#region Slide Controls

            if (arrayOf(
                    State.AT_BOTTOM,
                    State.CLAW_DOWN,
                    State.MOVING_CLAW_DOWN,
                    State.MOVING_CLAW_UP
                ).contains(state)
            ) {
                intakeClaw.grabberRotatorAngle += (if (gamepad1.left_bumper) 1 else 0 - if (gamepad1.right_bumper) 1 else 0).toDouble()
                intakeSlide.power =
                    (if (gamepad1.dpad_left) 1 else 0 - if (gamepad1.dpad_right) 1 else 0) * INTAKE_SLIDE_SPEED
            }

            if (state == State.AT_TOP) {
                val outtakeSpeed = if (gamepad1.dpad_left) 1.0 else 0.0 - if (gamepad1.dpad_right) 1.0 else 0.0

                if (outtakeSpeed != 0.0) {
                    if (OUTTAKE_SLIDE_USING_ALL_MOTORS) {
                        outtakeSlide.allMotorSpeed =
                            outtakeSpeed * (if (outtakeSlideFast) OUTTAKE_SLIDE_FAST_POWER else OUTTAKE_SLIDE_SLOW_POWER)
                    } else {
                        outtakeSlide.speedMotorSpeed =
                            outtakeSpeed * (if (outtakeSlideFast) OUTTAKE_SLIDE_FAST_POWER else OUTTAKE_SLIDE_SLOW_POWER)
                    }
                } else {
                    outtakeSlide.targetPosition = outtakeSlideHoldPosition
                }
                telemetry.addData("Outtake Slide Hold Position", outtakeSlideHoldPosition)
                telemetry.addData("Outtake Slide Super Brake Enabled", OUTTAKE_SLIDE_SUPER_BRAKE)
                telemetry.addData(
                    "Outtake Slide Currently Holding",
                    outtakeSpeed == 0.0 && OUTTAKE_SLIDE_SUPER_BRAKE
                )
                telemetry.addData("Outtake Slide Using All Motors", OUTTAKE_SLIDE_USING_ALL_MOTORS)
                telemetry.addData("Outtake Slide Fast", outtakeSlideFast)
            }
            //#endregion


            currentAction = if (currentAction?.run(packet) == true) currentAction else null
            dash.sendTelemetryPacket(packet)

            //#region Telemetry
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
                "Pose", drive.localizer.pose.toString(),
            )
            telemetry.addData("Speed", speed)
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

            telemetry.addData(
                "Left Stick Y", gamepad1.left_stick_y.toDouble(),
            )
            telemetry.addData(
                "Left Stick X", gamepad1.left_stick_x.toDouble(),
            )
            telemetry.addData(
                "Right Stick X", gamepad1.right_stick_x.toDouble(),
            )
            telemetry.addData("State", state.name)
            telemetry.addData("Claw Action", currentAction?.toString() ?: "No action")
            telemetry.addData("Intake Slide Position", intakeSlide.position)
            telemetry.addData("Intake Slide Power", intakeSlide.power)
            telemetry.addData("Outtake Slide Position", outtakeSlide.position)
            telemetry.addData("Outtake Slide Target Position", outtakeSlide.targetPosition)
            telemetry.addData("Intake Claw Rotator Angle", intakeClaw.rotatorAngle)
            telemetry.addData("Intake Claw Grabber Position", intakeClaw.grabberPosition)
            telemetry.addData("Intake Claw Grabber Rotator Angle", intakeClaw.grabberRotatorAngle)
            telemetry.addData("Outtake Claw Rotator Angle", outtakeClaw.rotatorAngle)
            telemetry.addData("Outtake Claw Grabber Position", outtakeClaw.grabberPosition)
            telemetry.addData("Intake Slide Zero Sensor", intakeSlide.zeroSensor.isPressed)
            //#endregion

            telemetry.update()
            sleep(20)
        }
    }

    //        @Config
    companion object {
        @JvmField
        var OUTTAKE_SLIDE_USING_ALL_MOTORS = false

        @JvmField
        var OUTTAKE_SLIDE_SUPER_BRAKE = false

        @JvmField
        var OUTTAKE_SLIDE_SLOW_POWER = 0.5

        @JvmField
        var OUTTAKE_SLIDE_FAST_POWER = 0.2

        @JvmField
        var INTAKE_SLIDE_SPEED = 1.0

        @JvmField
        var DEFAULT_SPEED = 0.7

        @JvmField
        var SPEED_BOOST = 0.3

        @JvmField
        var SPEED_REDUCTION = 0.4
    }
}


