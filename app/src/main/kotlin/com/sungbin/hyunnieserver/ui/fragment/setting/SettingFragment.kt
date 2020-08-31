package com.sungbin.hyunnieserver.ui.fragment.setting

import androidx.fragment.app.Fragment


/**
 * Created by SungBin on 2020-08-31.
 */

class SettingFragment : Fragment() {

    companion object {
        private val instance by lazy {
            SettingFragment()
        }

        fun instance() = instance
    }

}