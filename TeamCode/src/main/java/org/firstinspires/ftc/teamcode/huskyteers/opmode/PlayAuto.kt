package org.firstinspires.ftc.teamcode.huskyteers.opmode

import android.os.Environment
import com.acmerobotics.roadrunner.PoseVelocity2d
import com.acmerobotics.roadrunner.Vector2d
import com.huskyteers.paths.StartInfo
import com.qualcomm.hardware.lynx.LynxModule
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.teamcode.huskyteers.HuskyOpMode
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.ObjectInputStream
import kotlin.math.abs
import kotlin.math.max

@Autonomous
class PlayAuto : HuskyOpMode(StartInfo(StartInfo.Color.RED, StartInfo.Position.FarFromBasket)) {
    var FILENAME: String = "FILE NAME HERE"


    var recording: ArrayList<HashMap<String, Double>> = ArrayList()
    val runtime: ElapsedTime = ElapsedTime()

    override fun runOpMode() {
        val allHubs = hardwareMap.getAll(LynxModule::class.java)
        for (hub in allHubs) {
            hub.bulkCachingMode = LynxModule.BulkCachingMode.AUTO
        }

        loadDatabase()
        waitForStart()
        if (opModeIsActive()) {
            runtime.reset()
            while (opModeIsActive()) {
                playRecording(recording)
            }
        }
    }


    //Reads the saved recording from file. You have to change the file path when saving and reading.
    private fun loadDatabase(): Boolean {
        val loadedProperly = false
        val path = String.format(
            "%s/FIRST/data/$FILENAME.fil",
            Environment.getExternalStorageDirectory().absolutePath
        )
        try {
            val file = File(path)
            val fis = FileInputStream(file)
            val ois = ObjectInputStream(fis)
            recording = ois.readObject() as ArrayList<HashMap<String, Double>>
            if (recording is ArrayList<*>) {
                telemetry.addData("Update", "It worked!")
            } else {
                telemetry.addData("Update", "Did not work smh")
            }
            ois.close()
        } catch (e: IOException) {
            telemetry.addData("Error", "IOException")
            e.printStackTrace()
        } catch (e: ClassNotFoundException) {
            telemetry.addData("Error", "ClassNotFoundException")
            e.printStackTrace()
        }
        telemetry.addData("recording", recording.toString())
        telemetry.update()
        return loadedProperly
    }


    //Think of each frame as a collection of every input the driver makes in one moment, saved like a frame in a video is
    private fun playRecording(recording: ArrayList<HashMap<String, Double>>) {
        //Gets the correct from from the recording


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
}
