package com.sungbin.hyunnieserver.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.WithFragmentBindings


/**
 * Created by SungBin on 2020-10-07.
 */

@AndroidEntryPoint
@WithFragmentBindings
abstract class BaseFragment : Fragment() {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        retainInstance = false
    }
}