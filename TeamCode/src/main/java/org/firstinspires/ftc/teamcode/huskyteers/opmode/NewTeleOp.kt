package org.firstinspires.ftc.teamcode.huskyteers.opmode

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.config.Config
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.Pose2d
import com.huskyteers.paths.StartInfo
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.huskyteers.HuskyOpMode
import org.firstinspires.ftc.teamcode.huskyteers.utils.GamepadUtils


@Config
@TeleOp
class NewTeleOp : HuskyOpMode(StartInfo.empty()) {
    private val dash: FtcDashboard = FtcDashboard.getInstance()

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


        gamepad1Utils.addRisingEdge("dpad_down") {
            // Move the intake claw down
            intakeClaw.rotateDown()
        }


        gamepad1Utils.addRisingEdge("dpad_up") {
            intakeClaw.rotateUp()
        }

        gamepad1Utils.addRisingEdge("x") {
            intakeClaw.close()
        }

        gamepad1Utils.addRisingEdge("y") {
            intakeClaw.open()
        }

        gamepad2Utils.addRisingEdge("dpad_down") {
            // Move the intake claw down
            outtakeClaw.rotateDown()
        }


        gamepad2Utils.addRisingEdge("dpad_up") {
            outtakeClaw.rotateUp()
        }

        gamepad2Utils.addRisingEdge("x") {
            outtakeClaw.close()

        }

        gamepad2Utils.addRisingEdge("y") {
            outtakeClaw.open()
        }

        gamepad2Utils.addRisingEdge("b") {
            outtakeSlide.resetEncoder()
        }


        //#endregion


        while (opModeIsActive() && !isStopRequested) {
            val packet = TelemetryPacket()

            gamepad1Utils.processUpdates(gamepad1)
            gamepad2Utils.processUpdates(gamepad2)

            outtakeSlide.checkBottom()

            //#region Driving
            val speed = 1.0

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

            intakeClaw.grabberRotatorAngle += (if (gamepad1.left_bumper) 1 else 0 - if (gamepad1.right_bumper) 1 else 0).toDouble()
            intakeSlide.power =
                (if (gamepad1.dpad_left) 1 else 0 - if (gamepad1.dpad_right) 1 else 0) * INTAKE_SLIDE_SPEED

            val outtakeSpeed = if (gamepad2.dpad_left) 1.0 else 0.0 - if (gamepad2.dpad_right) 1.0 else 0.0

            if (OUTTAKE_SLIDE_USING_ALL_MOTORS) {
                outtakeSlide.torqueMotorSpeed =
                    outtakeSpeed * 1.0
            } else {
                outtakeSlide.speedMotorSpeed =
                    outtakeSpeed * OUTTAKE_SLIDE_FAST_POWER
            }

            telemetry.addData("Outtake Slide Hold Position", outtakeSlideHoldPosition)
            telemetry.addData("Outtake Slide Super Brake Enabled", OUTTAKE_SLIDE_SUPER_BRAKE)
            telemetry.addData(
                "Outtake Slide Currently Holding",
                outtakeSpeed == 0.0 && OUTTAKE_SLIDE_SUPER_BRAKE
            )
            telemetry.addData("Outtake Slide Using All Motors", OUTTAKE_SLIDE_USING_ALL_MOTORS)


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

    @Config
    companion object {
        @JvmField
        var OUTTAKE_SLIDE_USING_ALL_MOTORS = false

        @JvmField
        var OUTTAKE_SLIDE_SUPER_BRAKE = false

        @JvmField
        var OUTTAKE_SLIDE_SLOW_POWER = 0.5

        @JvmField
        var OUTTAKE_SLIDE_FAST_POWER = 0.7

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


