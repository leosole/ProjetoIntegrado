package com.ufrj.projetointegrado

import android.bluetooth.BluetoothAdapter
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
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
            container, false
        )
        binding.buttonIniciar.setOnClickListener { view: View ->  // BOTÃO INICIAR
            when (testBT()) {  // Verifica estado do bluetooth
                0 -> view.findNavController().navigate(R.id.action_inicio_to_btOff)
                1 -> view.findNavController().navigate(R.id.action_inicio_to_carOff)
                else -> {
                    (activity as MainActivity?)?.startControle()
//                    view.findNavController().navigate(R.id.action_inicio_to_controle)
                }
            }
        }
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.overflow_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(
            item,
            requireView().findNavController()
        )
                || super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        val activity = activity as MainActivity?
        activity?.hideUpButton()
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