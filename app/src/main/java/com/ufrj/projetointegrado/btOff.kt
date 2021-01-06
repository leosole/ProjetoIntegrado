package com.ufrj.projetointegrado

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
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
        return binding.root
    }

}