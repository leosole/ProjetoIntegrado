package com.ufrj.projetointegrado

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.databinding.DataBindingUtil
import com.ufrj.projetointegrado.databinding.ActivityControleBinding
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
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
    private val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_controle)
        cs = binding.controleConstraint
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
            if(!start) {
                binding.buttonCalibrate.setText("Recalibrar")
                binding.instrCalibrate.setText(R.string.instrucao_recalib)
                start = true
                Log.i("Bluetooth", searchPairedDevices("HC-05")) // teste para achar o dispositivo
            }
            calibrate = true
        }
        set = ConstraintSet()
    }

    fun searchPairedDevices(name: String): String { // procura o módulo bluetooth pelo nome (HC-05)
        val pairedDevices: Set<BluetoothDevice>? = bluetoothAdapter?.bondedDevices
        var mac = "not found"
        pairedDevices?.forEach { device ->
            if (device.name == name) {
                mac = device.address
//                ConnectThread(device)  // inicia conexão
            }
        }
        return mac
    }

    private inner class ConnectThread(device: BluetoothDevice) : Thread() {
        val MY_UUID = UUID(1111, 1111)  // identificador -> usar o mesmo no carrinho
        private val mmSocket: BluetoothSocket? by lazy(LazyThreadSafetyMode.NONE) {
            device.createRfcommSocketToServiceRecord(MY_UUID)
        }
        override fun run() {
            bluetoothAdapter?.cancelDiscovery()
            mmSocket?.use { socket ->
                socket.connect()
//                manageMyConnectedSocket(socket)
            }
        }
        fun cancel() {
            try {
                mmSocket?.close()
            } catch (e: IOException) {
                Log.e("Bluetooth", "Could not close the client socket", e)
            }
        }
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

        toDegrees(orientation[0])
        val pitchAngle = toDegrees(orientation[1])
        val rollAngle = toDegrees(orientation[2])
        if (calibrate){ // calibração
            y0 = pitchAngle
            x0 = rollAngle
            val msg = "Origem alterada: (" + x0 +  ", " + y0 + ")"
            Log.i("Controle", msg)
            calibrate = false
        }
        if (start) {  // inicia controle
            generateCode(pitchAngle, rollAngle)
        }
    }

    fun toDegrees(radians: Float): Double {
        return Math.toDegrees(radians.toDouble())
    }


    fun generateCode(pitchAngle: Double, rollAngle: Double): String {
        val frontCircle = com.ufrj.projetointegrado.R.id.frontCircle
        set.clone(cs)

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
}

class MyBluetoothService(
    // handler that gets info from Bluetooth service
    private val handler: Handler
) {
    val MESSAGE_READ: Int = 0
    val MESSAGE_WRITE: Int = 1
    val MESSAGE_TOAST: Int = 2

    private inner class ConnectedThread(private val mmSocket: BluetoothSocket) : Thread() {

        private val mmInStream: InputStream = mmSocket.inputStream
        private val mmOutStream: OutputStream = mmSocket.outputStream
        private val mmBuffer: ByteArray = ByteArray(1024) // mmBuffer store for the stream

        override fun run() {
            var numBytes: Int // bytes returned from read()

            // Keep listening to the InputStream until an exception occurs.
            while (true) {
                // Read from the InputStream.
                numBytes = try {
                    mmInStream.read(mmBuffer)
                } catch (e: IOException) {
                    Log.d("Bluetooth", "Input stream was disconnected", e)
                    break
                }

                // Send the obtained bytes to the UI activity.
                val readMsg = handler.obtainMessage(
                    MESSAGE_READ, numBytes, -1,
                    mmBuffer)
                readMsg.sendToTarget()
            }
        }

        // Call this from the main activity to send data to the remote device.
        fun write(bytes: ByteArray) {
            try {
                mmOutStream.write(bytes)
            } catch (e: IOException) {
                Log.e("Bluetooth", "Error occurred when sending data", e)

                // Send a failure message back to the activity.
                val writeErrorMsg = handler.obtainMessage(MESSAGE_TOAST)
                val bundle = Bundle().apply {
                    putString("toast", "Couldn't send data to the other device")
                }
                writeErrorMsg.data = bundle
                handler.sendMessage(writeErrorMsg)
                return
            }

            // Share the sent message with the UI activity.
            val writtenMsg = handler.obtainMessage(
                MESSAGE_WRITE, -1, -1, bytes)
            writtenMsg.sendToTarget()
        }

        // Call this method from the main activity to shut down the connection.
        fun cancel() {
            try {
                mmSocket.close()
            } catch (e: IOException) {
                Log.e("Bluetooth", "Could not close the connect socket", e)
            }
        }
    }
}