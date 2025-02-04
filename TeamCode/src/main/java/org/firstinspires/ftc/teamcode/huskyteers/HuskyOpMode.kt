package org.firstinspires.ftc.teamcode.huskyteers

import com.acmerobotics.roadrunner.PoseVelocity2d
import com.acmerobotics.roadrunner.Vector2d
import com.huskyteers.paths.StartInfo
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.MecanumDrive
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

/**
 * Base class for any OpMode, whether it's TeleOp or Autonomous.
 */
abstract class HuskyOpMode(var startInfo: StartInfo) : LinearOpMode() {
    /**
     * Variables to store the position and orientation of the camera on the robot. Setting these
     * values requires a definition of the axes of the camera and robot:
     *
     *
     * Camera axes:
     * Origin location: Center of the lens
     * Axes orientation: +x right, +y down, +z forward (from camera's perspective)
     *
     *
     * Robot axes (this is typical, but you can define this however you want):
     * Origin location: Center of the robot at field height
     * Axes orientation: +x right, +y forward, +z upward
     *
     *
     * Position:
     * If all values are zero (no translation), that implies the camera is at the center of the
     * robot. Suppose your camera is positioned 5 inches to the left, 7 inches forward, and 12
     * inches above the ground - you would need to set the position to (-5, 7, 12).
     *
     *
     * Orientation:
     * If all values are zero (no rotation), that implies the camera is pointing straight up. In
     * most cases, you'll need to set the pitch to -90 degrees (rotation about the x-axis), meaning
     * the camera is horizontal. Use a yaw of 0 if the camera is pointing forwards, +90 degrees if
     * it's pointing straight left, -90 degrees for straight right, etc. You can also set the roll
     * to +/-90 degrees if it's vertical, or 180 degrees if it's upside-down.
     */
//    private val cameraPosition = Position(DistanceUnit.INCH, 0.0, 0.0, 0.0, 0)
//    private val cameraOrientation = YawPitchRollAngles(AngleUnit.DEGREES, 0.0, -90.0, 0.0, 0)
    val drive: MecanumDrive = MecanumDrive(hardwareMap, startInfo.position.pose2d)
//    val visionPortal: VisionPortal by lazy {
//        VisionPortal.Builder().apply {
//            setCamera(hardwareMap.get(WebcamName::class.java, "Webcam 1"))
//            addProcessors(aprilTag, allianceColorBlob, neutralColorBlob)
//        }.build()
//    }
//    private val aprilTag: AprilTagProcessor by lazy {
//        AprilTagProcessor.Builder().setCameraPose(cameraPosition, cameraOrientation).build()
//    }
//    private val sharedColorBlobBuilder: ColorBlobLocatorProcessor.Builder by lazy {
//        ColorBlobLocatorProcessor.Builder() // use a predefined color match
//            .setContourMode(ColorBlobLocatorProcessor.ContourMode.EXTERNAL_ONLY) // exclude blobs inside blobs
//            .setRoi(
//                ImageRegion.asUnityCenterCoordinates(
//                    -1.0,
//                    1.0,
//                    1.0,
//                    -1.0
//                )
//            ) // search central 1/4 of camera view
//            .setDrawContours(true) // Show contours on the Stream Preview
//            .setBlurSize(5)
//    }
//    private val allianceColorBlob: ColorBlobLocatorProcessor by lazy {
//        sharedColorBlobBuilder
//            .setTargetColorRange(if (startInfo.color == StartInfo.Color.BLUE) ColorRange.BLUE else ColorRange.RED)
//            .build()
//    }
//    private val neutralColorBlob: ColorBlobLocatorProcessor by lazy {
//        sharedColorBlobBuilder
//            .setTargetColorRange(ColorRange.YELLOW)
//            .build()
//    }


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
