package org.firstinspires.ftc.teamcode.huskyteers.utils

import com.qualcomm.robotcore.hardware.Gamepad
import java.util.function.Consumer

class GamepadUtils {
    private val currentGamepad = Gamepad()
    private val previousGamepad = Gamepad()
    private val risingEdgeDetectors: MutableList<Detector> = ArrayList()
    private val fallingEdgeDetectors: MutableList<Detector> = ArrayList()

    private fun getButton(button: String, gamepad: Gamepad): Boolean {
        try {
            return gamepad.javaClass.getField(button)[gamepad] as Boolean
        } catch (e: NoSuchFieldException) {
            println("Invalid button name")
            throw NoSuchElementException("invalid button")
        } catch (e: IllegalAccessException) {
            println("Invalid button name")
            throw NoSuchElementException("invalid button")
        } catch (e: NullPointerException) {
            println("Invalid button name")
            throw NoSuchElementException("invalid button")
        }
    }

    /**
     * Watches both rising edge and falling edge.
     *
     * @param button   The button to watch
     * @param callback The callback to the function when pressed or released
     */
    fun addHoldDetector(button: String, callback: Consumer<Boolean?>) {
        risingEdgeDetectors.add(Detector(button, callback))
        fallingEdgeDetectors.add(Detector(button, callback))
    }

    /**
     * Add a detector for detecting a button press
     *
     * @param button   The button to watch
     * @param callback The callback to the function when pressed
     */
    fun addRisingEdge(button: String, callback: Consumer<Boolean?>) {
        risingEdgeDetectors.add(Detector(button, callback))
    }

    /**
     * Add a detector for detecting a button release
     *
     * @param button   The button to watch
     * @param callback The callback to the function when released
     */
    fun addFallingEdge(button: String, callback: Consumer<Boolean?>) {
        fallingEdgeDetectors.add(Detector(button, callback))
    }

    fun processUpdates(gamepad: Gamepad?) {
        previousGamepad.copy(currentGamepad)
        currentGamepad.copy(gamepad)
        for (detector in risingEdgeDetectors) {
            if (getButton(detector.button, currentGamepad) && !getButton(
                    detector.button,
                    previousGamepad
                )
            ) {
                detector.callback.accept(true)
            }
        }
        for (detector in fallingEdgeDetectors) {
            if (getButton(detector.button, currentGamepad) && !getButton(
                    detector.button,
                    previousGamepad
                )
            ) {
                detector.callback.accept(false)
            }
        }
    }

    private class Detector(var button: String, var callback: Consumer<Boolean?>)
}