package com.ufrj.projetointegrado

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import com.ufrj.projetointegrado.databinding.FragmentCarOffBinding

/**
 * A simple [Fragment] subclass.
 * Use the [CarOff.newInstance] factory method to
 * create an instance of this fragment.
 */
class CarOff : Fragment(){

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentCarOffBinding>(inflater, R.layout.fragment_car_off, container, false)
        binding.buttonConnectBT.setOnClickListener (  // BOTÃO CONECTAR BT
                Navigation.createNavigateOnClickListener(R.id.action_carOff_to_controle)
        )
        return binding.root
    }

}