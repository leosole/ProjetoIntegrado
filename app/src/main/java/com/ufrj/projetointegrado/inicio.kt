package com.ufrj.projetointegrado

import android.bluetooth.BluetoothAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
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
        val binding = DataBindingUtil.inflate<FragmentInicioBinding>(
                inflater,
                R.layout.fragment_inicio,
                container, false)
        binding.buttonIniciar.setOnClickListener { view: View ->  // BOTÃO INICIAR
            val btState = testBT()
            if (btState == 0) {
                Navigation.createNavigateOnClickListener(R.id.action_inicio_to_btOff)
            }
            else if( btState == 1){
                Navigation.createNavigateOnClickListener(R.id.action_inicio_to_carOff)
            }
            else {
                Navigation.createNavigateOnClickListener(R.id.action_inicio_to_controle)
            }
//            when (testBT()) {  // Verifica estado do bluetooth
//                0 -> Navigation.createNavigateOnClickListener(R.id.action_inicio_to_btOff)
//                1 -> Navigation.createNavigateOnClickListener(R.id.action_inicio_to_carOff)
//                else -> Navigation.createNavigateOnClickListener(R.id.action_inicio_to_controle)
//            }
        }
        return binding.root
    }
    fun testBT(): Int {
        val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        return if (!bluetoothAdapter.isEnabled) {
            // Bluetooth desativado
            0
        } else {
            // Verificar se carrinho está conectado
            // Carrinho conectado
            2
        }

    }

}