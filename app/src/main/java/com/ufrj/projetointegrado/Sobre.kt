package com.ufrj.projetointegrado

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.ufrj.projetointegrado.databinding.FragmentSobreBinding

/**
 * A simple [Fragment] subclass.
 * Use the [Sobre.newInstance] factory method to
 * create an instance of this fragment.
 */
class Sobre : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentSobreBinding>(inflater, R.layout.fragment_sobre, container, false)
        return binding.root
    }

}