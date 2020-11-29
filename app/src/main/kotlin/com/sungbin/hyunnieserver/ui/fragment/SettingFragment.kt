package com.sungbin.hyunnieserver.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mahfa.dnswitch.DayNightSwitchAnimListener
import com.sungbin.hyunnieserver.databinding.FragmentSettingBinding


/**
 * Created by SungBin on 2020-08-31.
 */

class SettingFragment : Fragment() {

    private lateinit var binding: FragmentSettingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.vDns.run {
            setDuration(450)
            setListener { is_night ->
                if (is_night) binding.vNightBackground.alpha = 1f
            }
            setAnimListener(object : DayNightSwitchAnimListener {
                override fun onAnimStart() {}
                override fun onAnimEnd() {}
                override fun onAnimValueChanged(value: Float) {
                    binding.vNightBackground.alpha = value
                }
            })
        }
    }

}