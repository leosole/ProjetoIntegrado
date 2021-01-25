package com.ufrj.projetointegrado

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.ufrj.projetointegrado.databinding.FragmentControleBinding
import java.util.*

/**
 * A simple [Fragment] subclass.
 * Use the [Controle.newInstance] factory method to
 * create an instance of this fragment.
 */
abstract class Controle : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentControleBinding>(inflater, R.layout.fragment_controle, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        val activity = activity as MainActivity?
        activity?.showUpButton()
    }
}

