package com.ufrj.projetointegrado

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
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

    override fun onResume() {
        super.onResume()
        val activity = activity as MainActivity?
        activity?.showUpButton()
    }
}