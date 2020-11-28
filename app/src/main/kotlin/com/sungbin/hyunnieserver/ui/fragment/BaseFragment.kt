package com.sungbin.hyunnieserver.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment


/**
 * Created by SungBin on 2020-10-07.
 */

abstract class BaseFragment : Fragment() {
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        retainInstance = false
    }
}