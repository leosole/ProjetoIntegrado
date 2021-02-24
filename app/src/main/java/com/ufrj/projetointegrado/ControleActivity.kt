package com.ufrj.projetointegrado

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.databinding.DataBindingUtil
import com.ufrj.projetointegrado.databinding.ActivityControleBinding
import java.io.IOException
import java.util.*


class ControleActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager

    private val rotationMatrix = FloatArray(9)
    private val orientationAngles = FloatArray(3)
    private lateinit var binding: ActivityControleBinding
    private lateinit var cs: ConstraintLayout  // guarda informações do layout
    private lateinit var set: ConstraintSet  // altera posição da bolinha
    private var x0 = 0.0  // 0 da calibração
    private var y0 = 0.0  // 0 da calibração
    private var calibrate = false  // calibra a rotação
    private var start = false  // inicia o controle do carrinho

    var btAdapter: BluetoothAdapter? = null
    var btSocket: BluetoothSocket? = null
    val myUUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
    private lateinit var ARDUINO_MAC_ADDRESS: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_controle)
        cs = binding.controleConstraint
        val buttonCalibrate = com.ufrj.projetointegrado.R.id.buttonCalibrate
        set = ConstraintSet()
        set.clone(cs)
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        start = false
        calibrate = false
        sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)?.also { rotationSensor ->
            sensorManager.registerListener(
                this,
                rotationSensor,
                SensorManager.SENSOR_DELAY_NORMAL,
                SensorManager.SENSOR_DELAY_UI
            )
        }
        setSupportActionBar(binding.myToolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        binding.buttonCalibrate.setOnClickListener {
            if (!start) {
                binding.buttonCalibrate.text = "Recalibrar"
                set.setVerticalBias(buttonCalibrate, 0.78f)
                set.applyTo(cs)
                binding.instrCalibrate.setText(R.string.instrucao_recalib)
                binding.buttonParar.setVisibility(View.VISIBLE)
                start = true
            }
            calibrate = true
        }
        binding.buttonParar.setOnClickListener{
            set.setVerticalBias(buttonCalibrate, 0.8f)
            set.applyTo(cs)
            binding.buttonCalibrate.text = "Calibrar"
            binding.instrCalibrate.setText(R.string.instrucao_calib)
            start = false
            sendCommandAndUpdateUI(y0, x0)
            binding.buttonParar.setVisibility(View.INVISIBLE)
        }

        ARDUINO_MAC_ADDRESS = searchPairedDevices("HC-05")
        if(ARDUINO_MAC_ADDRESS != "not found") {
            connectBluetoothDevice(ARDUINO_MAC_ADDRESS)
        }
        else {
            val text = "Carrinho não encontrado"
            val duration = Toast.LENGTH_LONG
            val toast = Toast.makeText(applicationContext, text, duration)
            toast.show()
            this.finish()
        }
    }

    fun searchPairedDevices(name: String): String { // procura o módulo bluetooth pelo nome (HC-05)
        val pairedDevices: Set<BluetoothDevice>? = btAdapter?.bondedDevices
        var mac = "not found"
        pairedDevices?.forEach { device ->
            if (device.name == name){
                mac = device.address
            }
        }
        return mac
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

        val pitchAngle = Math.toDegrees(orientation[1].toDouble())
        val rollAngle = Math.toDegrees(orientation[2].toDouble())
        if (calibrate) { // calibração
            y0 = pitchAngle
            x0 = rollAngle
            val msg = "Origem alterada: (" + x0 + ", " + y0 + ")"
            Log.i("Controle", msg)
            calibrate = false
        }
        if (start) {  // inicia controle
            sendCommandAndUpdateUI(pitchAngle, rollAngle)
        }
    }

    fun connectBluetoothDevice(macAddress: String) {
        try {
            if (btSocket == null) {
                btAdapter = BluetoothAdapter.getDefaultAdapter()

                // This will connect the device with address as passed
                val btDevice: BluetoothDevice = btAdapter!!.getRemoteDevice(macAddress)
                btSocket = btDevice.createInsecureRfcommSocketToServiceRecord(myUUID)
                BluetoothAdapter.getDefaultAdapter().cancelDiscovery()

                btSocket!!.connect()
            }
        } catch (e: IOException) {
            btSocket = null
        }
    }

    fun sendBluetoothCommand(command: String) {
        if (btSocket != null) {
            try {
                btSocket!!.outputStream.write(command.toByteArray())
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun sendCommandAndUpdateUI(pitchAngle: Double, rollAngle: Double) {
        val ang1 = 8
        val ang2 = 16

        var x = 0
        if (rollAngle > x0 + ang1) {
            x = 1
        }
        if (rollAngle < x0 - ang1) {
            x = -1
        }
        if (rollAngle > x0 + ang2) {
            x = 2
        }
        if (rollAngle < x0 - ang2) {
            x = -2
        }

        var y = 0
        if (pitchAngle > y0 + ang1) {
            y = 1
        }
        if (pitchAngle < y0 - ang1) {
            y = -1
        }
        if (pitchAngle > y0 + ang2) {
            y = 2
        }
        if (pitchAngle < y0 - ang2) {
            y = -2
        }

        val frontCircle = com.ufrj.projetointegrado.R.id.frontCircle
        set.clone(cs)

        when { // v_roda_esquerda v_roda_direita
            x == 2 && y == 0 -> { // 2 0
                set.setHorizontalBias(frontCircle, 1.00f)
                set.setVerticalBias(frontCircle, 0.50f)
                sendBluetoothCommand("+2+0")
            }
            x == 1 && y == 0 -> { // 1 0
                set.setHorizontalBias(frontCircle, 0.75f)
                set.setVerticalBias(frontCircle, 0.50f)
                sendBluetoothCommand("+1+0")
            }
            x == -1 && y == 0 -> { // 0 1
                set.setHorizontalBias(frontCircle, 0.25f)
                set.setVerticalBias(frontCircle, 0.50f)
                sendBluetoothCommand("+0+1")
            }
            x == -2 && y == 0 -> { // 0 2
                set.setHorizontalBias(frontCircle, 0.00f)
                set.setVerticalBias(frontCircle, 0.50f)
                sendBluetoothCommand("+0+2")
            }
            x == -1 && y == -1 -> { // 0 -1
                set.setHorizontalBias(frontCircle, 0.25f)
                set.setVerticalBias(frontCircle, 0.65f)
                sendBluetoothCommand("+0-1")
            }
            x == 1 && y == -1 -> { // -1 0
                set.setHorizontalBias(frontCircle, 0.75f)
                set.setVerticalBias(frontCircle, 0.65f)
                sendBluetoothCommand("-1+0")
            }
            x == 0 && y == 2 -> { // 2 2
                set.setHorizontalBias(frontCircle, 0.50f)
                set.setVerticalBias(frontCircle, 0.00f)
                sendBluetoothCommand("+2+2")
            }
            x == 0 && y == 1 -> { // 1 1
                set.setHorizontalBias(frontCircle, 0.50f)
                set.setVerticalBias(frontCircle, 0.25f)
                sendBluetoothCommand("+1+1")
            }
            x == 0 && y == -1 -> { // -1 -1
                set.setHorizontalBias(frontCircle, 0.50f)
                set.setVerticalBias(frontCircle, 0.75f)
                sendBluetoothCommand("-1-1")
            }
            x == 0 && y == -2 -> { // -2 -2
                set.setHorizontalBias(frontCircle, 0.50f)
                set.setVerticalBias(frontCircle, 1.00f)
                sendBluetoothCommand("-2-2")
            }
            x == 2 && y <= -1 -> { // -2 -1
                set.setHorizontalBias(frontCircle, 0.85f)
                set.setVerticalBias(frontCircle, 0.85f)
                sendBluetoothCommand("-2-1")
            }
            x == 1 && y == -2 -> { // -2 -1
                set.setHorizontalBias(frontCircle, 0.85f)
                set.setVerticalBias(frontCircle, 0.85f)
                sendBluetoothCommand("-2-1")
            }
            x == 2 && y >= 1 -> { // 2 1
                set.setHorizontalBias(frontCircle, 0.85f)
                set.setVerticalBias(frontCircle, 0.15f)
                sendBluetoothCommand("+2+1")
            }
            x == 1 && y >= 1 -> { // 2 1
                set.setHorizontalBias(frontCircle, 0.85f)
                set.setVerticalBias(frontCircle, 0.15f)
                sendBluetoothCommand("+2+1")
            }
            x == -2 && y >= 1 -> { // 1 2
                set.setHorizontalBias(frontCircle, 0.15f)
                set.setVerticalBias(frontCircle, 0.15f)
                sendBluetoothCommand("+1+2")
            }
            x == -1 && y >= 1 -> { // 1 2
                set.setHorizontalBias(frontCircle, 0.15f)
                set.setVerticalBias(frontCircle, 0.15f)
                sendBluetoothCommand("+1+2")
            }
            x == -2 && y <= -1 -> { // -1 -2
                set.setHorizontalBias(frontCircle, 0.15f)
                set.setVerticalBias(frontCircle, 0.85f)
                sendBluetoothCommand("-1-2")
            }
            x == -1 && y == -2 -> { // -1 -2
                set.setHorizontalBias(frontCircle, 0.15f)
                set.setVerticalBias(frontCircle, 0.85f)
                sendBluetoothCommand("-1-2")
            }
            else -> { // 0 0
                set.setHorizontalBias(frontCircle, 0.50f)
                set.setVerticalBias(frontCircle, 0.50f)
                sendBluetoothCommand("+0+0")
            }
        }

        set.applyTo(cs)
    }
}