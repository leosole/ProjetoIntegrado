package com.ufrj.projetointegrado

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.ufrj.projetointegrado.databinding.FragmentControleBinding

/**
 * A simple [Fragment] subclass.
 * Use the [Controle.newInstance] factory method to
 * create an instance of this fragment.
 */
class Controle : Fragment() {

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