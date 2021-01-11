package com.ufrj.projetointegrado

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
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

}