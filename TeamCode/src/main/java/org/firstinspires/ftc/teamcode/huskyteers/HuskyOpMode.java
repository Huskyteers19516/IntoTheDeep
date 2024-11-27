package org.firstinspires.ftc.teamcode.huskyteers;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import org.firstinspires.ftc.teamcode.MecanumDrive;
import org.firstinspires.ftc.teamcode.huskyteers.hardware.ArmSlide;
import org.firstinspires.ftc.teamcode.huskyteers.hardware.Claw;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import org.firstinspires.ftc.vision.opencv.ColorBlobLocatorProcessor;
import org.firstinspires.ftc.vision.opencv.ColorRange;
import org.firstinspires.ftc.vision.opencv.ImageRegion;
import org.opencv.core.RotatedRect;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;

import kotlin.NotImplementedError;

/**
 * Base class for any OpMode, whether it's teleop or autonomous.
 */
abstract public class HuskyOpMode extends LinearOpMode {
    /**
     * Variables to store the position and orientation of the camera on the robot. Setting these
     * values requires a definition of the axes of the camera and robot:
     * <p>
     * Camera axes:
     * Origin location: Center of the lens
     * Axes orientation: +x right, +y down, +z forward (from camera's perspective)
     * <p>
     * Robot axes (this is typical, but you can define this however you want):
     * Origin location: Center of the robot at field height
     * Axes orientation: +x right, +y forward, +z upward
     * <p>
     * Position:
     * If all values are zero (no translation), that implies the camera is at the center of the
     * robot. Suppose your camera is positioned 5 inches to the left, 7 inches forward, and 12
     * inches above the ground - you would need to set the position to (-5, 7, 12).
     * <p>
     * Orientation:
     * If all values are zero (no rotation), that implies the camera is pointing straight up. In
     * most cases, you'll need to set the pitch to -90 degrees (rotation about the x-axis), meaning
     * the camera is horizontal. Use a yaw of 0 if the camera is pointing forwards, +90 degrees if
     * it's pointing straight left, -90 degrees for straight right, etc. You can also set the roll
     * to +/-90 degrees if it's vertical, or 180 degrees if it's upside-down.
     */
    private final Position cameraPosition = new Position(DistanceUnit.INCH, 0, 0, 0, 0);
    private final YawPitchRollAngles cameraOrientation = new YawPitchRollAngles(AngleUnit.DEGREES, 0, -90, 0, 0);
    public MecanumDrive drive;
    public ArmSlide armSlide;
    public Claw claw;
    public VisionPortal visionPortal;
    public AprilTagProcessor aprilTag;
    public ColorBlobLocatorProcessor allianceColorBlob;
    public ColorBlobLocatorProcessor neutralColorBlob;

    public HuskyOpMode() {}

    public void initColorBlob(StartInfo.Color color) {
        ColorBlobLocatorProcessor.Builder shared = new ColorBlobLocatorProcessor.Builder()// use a predefined color match
                .setContourMode(ColorBlobLocatorProcessor.ContourMode.EXTERNAL_ONLY)    // exclude blobs inside blobs
                .setRoi(ImageRegion.asUnityCenterCoordinates(-1.0, 1.0, 1.0, -1.0))  // search central 1/4 of camera view
                .setDrawContours(true)                        // Show contours on the Stream Preview
                .setBlurSize(5);
        allianceColorBlob = shared
                .setTargetColorRange(color.equals(StartInfo.Color.BLUE) ? ColorRange.BLUE : ColorRange.RED)
                .build();
        neutralColorBlob = shared
                .setTargetColorRange(ColorRange.YELLOW)
                .build();
    }

    public void initAprilTag() {
        aprilTag = new AprilTagProcessor.Builder().setCameraPose(cameraPosition, cameraOrientation).build();
    }

    public void initVisionPortal(StartInfo.Color color) {
        VisionPortal.Builder builder = new VisionPortal.Builder();

        builder.setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"));
        initAprilTag();
        initColorBlob(color);

        builder.addProcessor(aprilTag);
        builder.addProcessor(allianceColorBlob);

        visionPortal = builder.build();
    }

    public void instantiateMotors(Pose2d pose) {
        armSlide = new ArmSlide(hardwareMap);
        claw = new Claw(hardwareMap);
        drive = new MecanumDrive(hardwareMap, pose);
    }

    public void driveRobot(double drive, double strafe, double turn, double speed) {
        PoseVelocity2d pw = new PoseVelocity2d(new Vector2d(-drive * speed, strafe * speed), turn * speed);

        this.drive.setDrivePowers(pw);
    }

    public void fieldCentricDriveRobot(double gamepadLeftStickY, double gamepadLeftStickX, double gamepadRightStickX, double speed) {
        drive.updatePoseEstimate();

        Vector2d angleVector = this.drive.pose.heading.vec();
        double angle = -Math.atan2(angleVector.y, angleVector.x);

        double rotatedX = gamepadLeftStickX * Math.cos(angle) - gamepadLeftStickY * Math.sin(angle);
        double rotatedY = gamepadLeftStickX * Math.sin(angle) + gamepadLeftStickY * Math.cos(angle);

        driveRobot(rotatedY, rotatedX, gamepadRightStickX, speed);
    }

    public void localizeRobotUsingAprilTags() {
        ArrayList<AprilTagDetection> detections = aprilTag.getDetections();
        OptionalDouble averageX = detections.stream().mapToDouble(aprilTagDetection -> aprilTagDetection.robotPose.getPosition().x).average();
        OptionalDouble averageY = detections.stream().mapToDouble(aprilTagDetection -> aprilTagDetection.robotPose.getPosition().y).average();
        OptionalDouble averageYaw = detections.stream().mapToDouble(aprilTagDetection -> aprilTagDetection.robotPose.getOrientation().getYaw()).average();
        if (averageX.isPresent() && averageY.isPresent() && averageYaw.isPresent()) {
            this.drive.pose = new Pose2d(averageX.getAsDouble(), averageY.getAsDouble(), averageYaw.getAsDouble());
        }
    }

    public void alignClawToSample() {
        List<ColorBlobLocatorProcessor.Blob> blobs = allianceColorBlob.getBlobs();
        if (blobs.size() > 0) {
            ColorBlobLocatorProcessor.Blob blob = blobs.get(0);
            // Information on opencv bounding box: https://theailearner.com/tag/cv2-minarearect/
            // https://docs.opencv.org/4.x/dd/d49/tutorial_py_contour_features.html
            RotatedRect box = blob.getBoxFit();
            if (box.size.height < box.size.width) {
                // The bounding box is horizontal
                claw.rotateClaw(-1 * box.angle + 90);
            } else {
                // The bounding box is vertical
                claw.rotateClaw(-1 * box.angle);
            }
        }
    }
}
