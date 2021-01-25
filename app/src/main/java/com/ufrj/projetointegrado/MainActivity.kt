package com.ufrj.projetointegrado

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.ufrj.projetointegrado.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity(){
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setSupportActionBar(binding.myToolbar)
    }
    fun showUpButton() {  // Mostra seta para voltar
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    fun hideUpButton() {  // Esconde seta para voltar
        supportActionBar!!.setDisplayHomeAsUpEnabled(false)
    }

    override fun onSupportNavigateUp(): Boolean{
        val navController = this.findNavController(R.id.navigation_fragment)
        return navController.navigateUp()
    }

    fun startControle() {
        val intent = Intent(this, ControleActivity::class.java)
        startActivity(intent)
    }

}