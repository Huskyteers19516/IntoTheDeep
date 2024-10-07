package com.huskyteers.paths;

import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;

public class Paths {
    // 1) Get the Data from the Camera via OpenCV
    // This displays the live camera preview on the robot preview screen
    int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
    // This defines the web Camera name and which web camera is used
    WebcamName webcamName = hardwareMap.get(WebcamName.class, "NAME_OF_CAMERA_IN_CONFIG_FILE");
    OpenCvCamera camera = OpenCvCameraFactory.getInstance().createWebcam(webcamName, cameraMonitorViewId);
    camera.setPipeline(yourPipeline);
    // An Asynchronous web cam will now be initialized.
    camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener())
    {
        @Override
        public void onOpened()
        {
            camera.setViewportRenderer(OpenCvCamera.ViewportRenderer.GPU_ACCELERATED);
            camera.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT);
        }
        @Override
        public void onError(int errorCode)
        {
            /*
             * This will be called if the camera could not be opened
             */
        }
    }
    // .... Ok the Camera Library is not added in yet....
    // PLace Holder Value!
    // This is for moving the robot towards the block/target
    private boolean move = true;
    public void distanceFromTarget(int distance, int orientation) {
        // Example initialization
        int distanceThreshold = 2; // Example Threshold
        int orientationThreshold = 3;
        int x = 0;
        int y = 0;
        boolean distanceTarget = false;
        boolean orientationTarget = false;
        // Distance Thresholds

        // FOR LOOP IMPLEMENTATION!!
        while (distance > distanceThreshold) {
            x = distance * math.cos(orientation); // might be wrong idea is to use trig functions
            y = distance * math.sin(orientation); // might be wrong idea is to use trig functions
            movement(x, y);
        }
        while (orientation > orientationThreshold) {
            // I don't know like turn left or right

            // If Left
            changeOrientation(actionBuilder, math.toRadians(-orientation));
            //

            // If Right
            changeOrientation(actionBuilder, math.toRadians(orientation));
            //
        }

        // ClawUsed singular IMPLEMENTATION!!
        if (move) {
            if (distance > distanceThreshold) {
                x = distance * math.cos(orientation); // might be wrong idea is to use trig functions
                y = distance * math.sin(orientation); // might be wrong idea is to use trig functions
                movement(x, y);
            }
            if (orientation > orientationThreshold) {
                // I don't know like turn left or right

                // If Left
                changeOrientation(actionBuilder, math.toRadians(-orientation));
                //

                // If Right
                changeOrientation(actionBuilder, math.toRadians(orientation));
                //
            }
            move = false;
        }

        if (distance < distanceThreshold) {
            distanceTarget = true;
        }
        if (orientation < orientationThreshold) {
            orientationTarget = true;
        }

    }
    // This is for doing the claw action of the robot itself
    public void clawAction(boolean distanceTarget, boolean orientationTarget) {
        continousMotor motor = hardwareMap.get(DcMotor.class, "motor");
        int x = 0;
        if (distanceTarget && orientationTarget) {
            motor.setPosition(x);
            move = true;
        }
    }
    public void movement(double x, double y) {
        public static TrajectoryActionBuilder examplePath(TrajectoryActionBuilder actionBuilder) {
            return actionBuilder.strafeTo(new Vector2d(10, 10));
        }
    }

    // Pure Psuedo Implementation
    // The openCV Camera value should give the net orientation and
    // the distance from the target.
    while (opmodeIsActive()) {
        // This is the distance from the target
        int distance = camera.getDistance();
        // This is the orientation from the target
        int orientation = camera.getOrientation();
        // This is the action builder
        TrajectoryActionBuilder actionBuilder = new TrajectoryActionBuilder();
        // This is the distance from the target

        // This should be it since infinite feedback loop. The distanceFromTarget function
        // will first determine if distance and orietnation is ideal. Then the clawAction
        // will set move to false. Then after claw this will make the move true which is feedback
        // loop back to distanceFromTarget so it should work.
        distanceFromTarget(distance, orientation);
        clawAction(distanceTarget, orientationTarget);
    }
    distanceFromTarget(camera.getDistance(), camera.getOrientation());

}
