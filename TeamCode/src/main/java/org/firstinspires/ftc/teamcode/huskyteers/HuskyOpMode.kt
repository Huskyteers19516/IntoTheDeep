package org.firstinspires.ftc.teamcode.huskyteers

import com.acmerobotics.roadrunner.PoseVelocity2d
import com.acmerobotics.roadrunner.Vector2d
import com.huskyteers.paths.StartInfo
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.MecanumDrive
import org.firstinspires.ftc.teamcode.huskyteers.hardware.IntakeClaw
import org.firstinspires.ftc.teamcode.huskyteers.hardware.IntakeSlide
import org.firstinspires.ftc.teamcode.huskyteers.hardware.OuttakeClaw
import org.firstinspires.ftc.teamcode.huskyteers.hardware.OuttakeSlide
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

/**
 * Base class for any OpMode, whether it's TeleOp or Autonomous.
 */
abstract class HuskyOpMode(var startInfo: StartInfo) : LinearOpMode() {
    val drive: MecanumDrive = MecanumDrive(hardwareMap, startInfo.position.pose2d)
    val intakeClaw by lazy {
        IntakeClaw(hardwareMap)
    }
    val intakeSlide by lazy {
        IntakeSlide(hardwareMap)
    }
    val outtakeClaw by lazy {
        OuttakeClaw(hardwareMap)
    }
    val outtakeSlide by lazy {
        OuttakeSlide(hardwareMap)
    }


    fun driveRobot(drive: Double, strafe: Double, turn: Double, speed: Double) {
        val pw = PoseVelocity2d(Vector2d(-drive * speed, strafe * speed), turn * speed)

        this.drive.setDrivePowers(pw)
    }

    fun fieldCentricDriveRobot(
        gamepadLeftStickY: Double,
        gamepadLeftStickX: Double,
        gamepadRightStickX: Double,
        speed: Double
    ) {
        drive.updatePoseEstimate()

        val angleVector = drive.localizer.pose.heading.vec()
        val angle = -atan2(angleVector.y, angleVector.x)

        val rotatedX = gamepadLeftStickX * cos(angle) - gamepadLeftStickY * sin(angle)
        val rotatedY = gamepadLeftStickX * sin(angle) + gamepadLeftStickY * cos(angle)

        driveRobot(rotatedY, rotatedX, gamepadRightStickX, speed)
    }

//    fun localizeRobotUsingAprilTags() {
//        val detections = aprilTag.detections
//        val averageX = detections.stream()
//            .mapToDouble { aprilTagDetection: AprilTagDetection -> aprilTagDetection.robotPose.position.x }
//            .average()
//        val averageY = detections.stream()
//            .mapToDouble { aprilTagDetection: AprilTagDetection -> aprilTagDetection.robotPose.position.y }
//            .average()
//        val averageYaw = detections.stream()
//            .mapToDouble { aprilTagDetection: AprilTagDetection -> aprilTagDetection.robotPose.orientation.yaw }
//            .average()
//        if (averageX.isPresent && averageY.isPresent && averageYaw.isPresent) {
//            drive.localizer.pose = Pose2d(averageX.asDouble, averageY.asDouble, averageYaw.asDouble)
//        }
//    }
//
//    val dumbSampleRotation: OptionalDouble
//        get() {
//            val blobs =
//                allianceColorBlob.blobs
//            if (blobs.isNotEmpty()) {
//                val blob = blobs[0]
//                // Information on opencv bounding box: https://theailearner.com/tag/cv2-minarearect/
//                // https://docs.opencv.org/4.x/dd/d49/tutorial_py_contour_features.html
//                val box = blob.boxFit
//                return OptionalDouble.of(box.angle)
//            }
//            return OptionalDouble.empty()
//        }
//
//    val sampleRotation: OptionalDouble
//        get() {
//            val blobs =
//                allianceColorBlob.blobs
//            if (blobs.isNotEmpty()) {
//                val blob = blobs[0]
//                // Information on opencv bounding box: https://theailearner.com/tag/cv2-minarearect/
//                // https://docs.opencv.org/4.x/dd/d49/tutorial_py_contour_features.html
//                val box = blob.boxFit
//                return if (box.size.height < box.size.width) {
//                    // The bounding box is horizontal
//                    OptionalDouble.of(box.angle + 90)
//                } else {
//                    // The bounding box is vertical
//                    OptionalDouble.of(box.angle)
//                }
//            }
//            return OptionalDouble.empty()
//        }
}
