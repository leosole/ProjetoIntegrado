package com.ufrj.projetointegrado

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import com.ufrj.projetointegrado.databinding.FragmentInicioBinding

/**
 * A simple [Fragment] subclass.
 * Use the [inicio.newInstance] factory method to
 * create an instance of this fragment.
 */
class inicio : Fragment(){

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentInicioBinding>(inflater, R.layout.fragment_inicio, container, false)
        binding.buttonIniciar.setOnClickListener (
            Navigation.createNavigateOnClickListener(R.id.action_inicio_to_btOff)
        )
        return binding.root
    }

}