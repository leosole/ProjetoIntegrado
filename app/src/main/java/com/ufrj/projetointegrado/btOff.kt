package com.ufrj.projetointegrado

import android.bluetooth.BluetoothAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.ufrj.projetointegrado.databinding.FragmentBtOffBinding

/**
 * A simple [Fragment] subclass.
 * Use the [btOff.newInstance] factory method to
 * create an instance of this fragment.
 */
class btOff : Fragment(){

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentBtOffBinding>(inflater, R.layout.fragment_bt_off, container, false)
        binding.buttonConnectBT.setOnClickListener { view : View -> // BOTÃO CONECTAR BT
            binding.buttonConnectBT.setText("Conectando...")  // Troca texto do botão
            var myBT: BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
            myBT.enable()  // Ativa BT
            while (!myBT.isEnabled) {}  // Espera BT ativar para avançar
            view.findNavController().navigate(R.id.action_btOff_to_controle)  //  Troca de tela
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        val activity = activity as MainActivity?
        activity?.showUpButton()
    }

}