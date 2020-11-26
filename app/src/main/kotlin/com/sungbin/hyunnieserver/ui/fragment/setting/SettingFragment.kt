package com.sungbin.hyunnieserver.ui.fragment.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sungbin.hyunnieserver.databinding.TestFragmentBinding
import com.sungbin.hyunnieserver.ui.fragment.BaseFragment


/**
 * Created by SungBin on 2020-08-31.
 */

class SettingFragment : BaseFragment() {

    companion object {
        private lateinit var settingFragment: SettingFragment

        fun instance(): SettingFragment {
            if (!::settingFragment.isInitialized) {
                settingFragment = SettingFragment()
            }
            return settingFragment
        }
    }

    private var _binding: TestFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = TestFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.tvTest.text = "SettingFragment"
    }

}