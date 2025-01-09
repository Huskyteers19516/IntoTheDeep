package org.firstinspires.ftc.teamcode.huskyteers.opmode

import android.os.Environment
import com.acmerobotics.roadrunner.PoseVelocity2d
import com.acmerobotics.roadrunner.Vector2d
import com.huskyteers.paths.StartInfo
import com.qualcomm.hardware.lynx.LynxModule
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.teamcode.huskyteers.HuskyOpMode
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.ObjectOutputStream
import kotlin.math.abs
import kotlin.math.max

@TeleOp(name = "Record_Autonomous")
class RecordAuto : HuskyOpMode(StartInfo(StartInfo.Color.RED, StartInfo.Position.FarFromBasket)) {
    var FILENAME: String = "YOUR AUTO NAME HERE"

    var recordingLength: Int = 30

    //List of each "Frame" of the recording | Each frame has multiple saved values that are needed to fully visualize it
    var recording: ArrayList<HashMap<String, Double>> = ArrayList()

    val runtime: ElapsedTime = ElapsedTime()
    var isPlaying: Boolean = false
    var frameCounter: Int = 0
    var robotState: Int = 0

    override fun runOpMode() {
        //Increasing efficiency in getting data from the robot

        val allHubs = hardwareMap.getAll(LynxModule::class.java)
        for (hub in allHubs) {
            hub.bulkCachingMode = LynxModule.BulkCachingMode.AUTO
        }

        telemetry.addData("Status", "Waiting to Start")
        telemetry.update()

        waitForStart()

        if (opModeIsActive()) {
            while (opModeIsActive()) {
                //Before recording, gives driver a moment to get ready to record
                //Once the start button is pressed, recording will start

                if (gamepad1.start && robotState == 0) {
                    robotState = 1
                    runtime.reset()
                    telemetry.addData("Status", "Recording")
                    telemetry.addData(
                        "Time until recording end",
                        (recordingLength - runtime.time()).toString() + ""
                    )
                } else if (robotState == 0) {
                    telemetry.addData("Status", "Waiting to start recording")
                    telemetry.addData("Version", "1")
                } else if (robotState == 1) {
                    if (recordingLength - runtime.time() > 0) {
                        telemetry.addData("Status", "Recording")
                        telemetry.addData(
                            "Time until recording end",
                            (recordingLength - runtime.time()).toString() + ""
                        )
                        val values = robotMovement()
                        recording.add(values)
                    } else {
                        robotState = 2
                    }
                } else if (robotState == 2) {
                    telemetry.addData("Status", "Waiting to play Recording" + recording.size)
                    telemetry.addData("Time", runtime.time().toString() + "")
                    if (gamepad1.start) {
                        runtime.reset()
                        robotState = 3
                        telemetry.addData("Status", "Playing Recording")
                        telemetry.update()
                        isPlaying = true
                        playRecording(recording)
                    }
                } else if (robotState == 3) {
                    if (gamepad1.x) {
                        isPlaying = false
                    }
                    if (isPlaying) {
                        playRecording(recording)
                    } else {
                        robotState = 4
                        telemetry.addData("Status", "Done Recording play-back")
                        telemetry.addData("Save to file", "Press start to save")
                        telemetry.update()
                    }
                } else if (robotState == 4) {
                    if (gamepad1.start) {
                        telemetry.addData("Status", "Saving File")
                        val recordingIsSaved = false
                        val path = String.format(
                            "%s/FIRST/data/$FILENAME.fil",
                            Environment.getExternalStorageDirectory().absolutePath
                        )



                        telemetry.clearAll()
                        telemetry.addData("Status", saveRecording(recording, path))
                        telemetry.update()
                    }
                }

                telemetry.update()
            }
        }
    }

    //Writes the recording to file
    fun saveRecording(recording: ArrayList<HashMap<String, Double>>?, path: String): String {
        var rv = "Save Complete"

        try {
            val file = File(path)

            val fos = FileOutputStream(file)
            val oos = ObjectOutputStream(fos)

            oos.writeObject(recording)
            oos.close()
        } catch (e: IOException) {
            rv = e.toString()
        }

        return rv
    }

    //Think of each frame as a collection of every input the driver makes in one moment, saved like a frame in a video is
    private fun playRecording(recording: ArrayList<HashMap<String, Double>>) {
        //Gets the correct from from the recording

        //The connection between the robot and the hub is not very consistent, so I just get the inputs from the closest timestamp
        //and use that

        var largestTime = 0.0
        var largestNum = 0
        var correctTimeStamp = 0
        for (i in recording.indices) {
            if (recording[i]["time"]!! > largestTime) {
                if (recording[i]["time"]!! <= runtime.time()) {
                    largestTime = recording[i]["time"]!!
                    largestNum = i
                } else {
                    correctTimeStamp = largestNum
                }
            }
        }
        //Only used inputs are saved to the final recording, the file is too large if every single timestamp is saved.
        telemetry.addData("correctTimeStamp", correctTimeStamp.toString() + "")
        telemetry.update()
        val values = recording[correctTimeStamp]

        val forwardBackwardValue = values.getOrDefault("rotY", 0.0)
        val leftRightValue = values.getOrDefault("rotX", 0.0)
        val turningValue = values.getOrDefault("rx", 0.0)

        val highestValue =
            max(abs(forwardBackwardValue) + abs(leftRightValue) + abs(turningValue), 1.0)

        //Calculates amount of power for each wheel to get the desired outcome
        //E.G. You pressed the left joystick forward and right, and the right joystick right, you strafe diagonally while at the same time turning right, creating a circular strafing motion.
        //E.G. You pressed the left joystick forward, and the right joystick left, you drive like a car and turn left
        drive.setDrivePowers(
            PoseVelocity2d(
                Vector2d(forwardBackwardValue, leftRightValue),
                turningValue
            )
        )
    }

    //Simple robot movement
    //Slowed to half speed so movements are more accurate
    private fun robotMovement(): HashMap<String, Double> {
        frameCounter++
        val values = HashMap<String, Double>()
        val highestValue: Double

        var forwardBackwardValue =
            -gamepad1.left_stick_y.toDouble() //Controls moving forward/backward
        var leftRightValue =
            gamepad1.left_stick_x * 1.1 //Controls strafing left/right       *the 1.1 multiplier is to counteract any imperfections during the strafing*
        var turningValue = gamepad1.right_stick_x.toDouble() //Controls turning left/right
        forwardBackwardValue /= 2.0
        leftRightValue /= 2.0
        turningValue /= 2.0

        values["rotY"] = forwardBackwardValue
        values["rotX"] = leftRightValue
        values["rx"] = turningValue
        values["time"] = runtime.time()

        //Makes sure power of each engine is not below 100% (Math cuts anything above 1.0 to 1.0, meaning you can lose values unless you change values)
        //This gets the highest possible outcome, and if it's over 1.0, it will lower all motor powers by the same ratio to make sure powers stay equal
        highestValue = max(abs(forwardBackwardValue) + abs(leftRightValue) + abs(turningValue), 1.0)

        //Calculates amount of power for each wheel to get the desired outcome
        //E.G. You pressed the left joystick forward and right, and the right joystick right, you strafe diagonally while at the same time turning right, creating a circular strafing motion.
        //E.G. You pressed the left joystick forward, and the right joystick left, you drive like a car and turn left
        drive.setDrivePowers(
            PoseVelocity2d(
                Vector2d(forwardBackwardValue, leftRightValue),
                turningValue
            )
        )

        return values
    }
}