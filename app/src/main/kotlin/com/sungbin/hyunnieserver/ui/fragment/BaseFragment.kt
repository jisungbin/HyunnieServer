package com.sungbin.hyunnieserver.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.WithFragmentBindings
import org.apache.commons.net.ftp.FTPClient
import javax.inject.Inject


/**
 * Created by SungBin on 2020-10-07.
 */

@AndroidEntryPoint
@WithFragmentBindings
abstract class BaseFragment : Fragment() {

    @Inject
    lateinit var client: FTPClient

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        retainInstance = false
    }
}