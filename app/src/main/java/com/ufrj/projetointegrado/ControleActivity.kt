package com.ufrj.projetointegrado

import android.R
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.text.Layout
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.databinding.DataBindingUtil
import com.ufrj.projetointegrado.databinding.ActivityControleBinding
import kotlin.math.abs
import kotlin.math.sign


class ControleActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager

    private val rotationMatrix = FloatArray(9)
    private val orientationAngles = FloatArray(3)
    private lateinit var binding: ActivityControleBinding
    private lateinit var cs: ConstraintLayout
    private lateinit var set: ConstraintSet

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, com.ufrj.projetointegrado.R.layout.activity_controle)
        cs = binding.controleConstraint
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

        sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)?.also { rotationSensor ->
            sensorManager.registerListener(
                this,
                rotationSensor,
                SensorManager.SENSOR_DELAY_NORMAL,
                SensorManager.SENSOR_DELAY_UI
            )
        }
        set = ConstraintSet()
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null) {
            return
        }

        if (event.sensor.type == Sensor.TYPE_ROTATION_VECTOR) {
            updateOrientationAngles(event.values)
        }
    }

    fun updateOrientationAngles(rotationVector: FloatArray) {
        SensorManager.getRotationMatrixFromVector(rotationMatrix, rotationVector)
        val orientation = SensorManager.getOrientation(rotationMatrix, orientationAngles)

        val azimuthAngle = toDegrees(orientation[0])
        val pitchAngle = toDegrees(orientation[1])
        val rollAngle = toDegrees(orientation[2])
        generateCode(pitchAngle, rollAngle)

    }

    fun toDegrees(radians: Float): Double {
        return Math.toDegrees(radians.toDouble())
    }


    fun generateCode(pitchAngle: Double, rollAngle: Double): String {
//        var frontCircle = binding.frontCircle
        var frontCircle = com.ufrj.projetointegrado.R.id.frontCircle
        set.clone(cs)

        val ang1 = 8
        val ang2 = 16

        var x = 0

        if (abs(rollAngle) > ang1) {
            x = 1

        }
        if (abs(rollAngle) > ang2) {
            x = 2;
        }
        x *= sign(rollAngle).toInt()

        var y = 0;
        if (abs(pitchAngle) > ang1) {
            y = 1;
        }
        if (abs(pitchAngle) > ang2) {
            y = 2;
        }
        y *= sign(pitchAngle).toInt()

        when{ // v_roda_esquerda v_roda_direita
            x == 2 && y == 0 -> { // 2 0
                set.setHorizontalBias(frontCircle, 1.00f)
                set.setVerticalBias(frontCircle, 0.50f)
            }
            x == 1 && y == 0 -> { // 1 0
                set.setHorizontalBias(frontCircle, 0.75f)
                set.setVerticalBias(frontCircle, 0.50f)
            }
            x == -1 && y == 0 -> { // 0 1
                set.setHorizontalBias(frontCircle, 0.25f)
                set.setVerticalBias(frontCircle, 0.50f)
            }
            x == -2 && y == 0 -> { // 0 2
                set.setHorizontalBias(frontCircle, 0.00f)
                set.setVerticalBias(frontCircle, 0.50f)
            }
            x == -1 && y == -1 -> { // 0 -1
                set.setHorizontalBias(frontCircle, 0.25f)
                set.setVerticalBias(frontCircle, 0.65f)
            }
            x == 1 && y == -1 -> { // -1 0
                set.setHorizontalBias(frontCircle, 0.75f)
                set.setVerticalBias(frontCircle, 0.65f)
            }
            x == 0 && y == 2 -> { // 2 2
                set.setHorizontalBias(frontCircle, 0.50f)
                set.setVerticalBias(frontCircle, 0.00f)
            }
            x == 0 && y == 1 -> { // 1 1
                set.setHorizontalBias(frontCircle, 0.50f)
                set.setVerticalBias(frontCircle, 0.25f)
            }
            x == 0 && y == -1 -> { // -1 -1
                set.setHorizontalBias(frontCircle, 0.50f)
                set.setVerticalBias(frontCircle, 0.75f)
            }
            x == 0 && y == -2 -> { // -2 -2
                set.setHorizontalBias(frontCircle, 0.50f)
                set.setVerticalBias(frontCircle, 1.00f)
            }
            x == 2 && y <= -1 -> { // -2 -1
                set.setHorizontalBias(frontCircle, 0.85f)
                set.setVerticalBias(frontCircle, 0.85f)
            }
            x == 1 && y == -2 -> { // -2 -1
                set.setHorizontalBias(frontCircle, 0.85f)
                set.setVerticalBias(frontCircle, 0.85f)
            }
            x == 2 && y >= 1 -> { // 2 1
                set.setHorizontalBias(frontCircle, 0.85f)
                set.setVerticalBias(frontCircle, 0.15f)
            }
            x == 1 && y >= 1 -> { // 2 1
                set.setHorizontalBias(frontCircle, 0.85f)
                set.setVerticalBias(frontCircle, 0.15f)
            }
            x == -2 && y >= 1 -> { // 1 2
                set.setHorizontalBias(frontCircle, 0.15f)
                set.setVerticalBias(frontCircle, 0.15f)
            }
            x == -1 && y >= 1 -> { // 1 2
                set.setHorizontalBias(frontCircle, 0.15f)
                set.setVerticalBias(frontCircle, 0.15f)
            }
            x == -2 && y <= -1 -> { // -1 -2
                set.setHorizontalBias(frontCircle, 0.15f)
                set.setVerticalBias(frontCircle, 0.85f)
            }
            x == -1 && y == -2 -> { // -1 -2
                set.setHorizontalBias(frontCircle, 0.15f)
                set.setVerticalBias(frontCircle, 0.85f)
            }
            else -> { // 0 0
                set.setHorizontalBias(frontCircle, 0.50f)
                set.setVerticalBias(frontCircle, 0.50f)
            }
        }
        set.applyTo(cs)
        return "%+d%+d".format(x, y)
    }

//    fun showUpButton() {  // Mostra seta para voltar
//        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
//    }
//
//    override fun onResume() {
//        super.onResume()
//        this.showUpButton()
//    }
}