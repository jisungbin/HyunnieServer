package com.sungbin.hyunnieserver.ui.fragment.download

import androidx.fragment.app.Fragment


/**
 * Created by SungBin on 2020-08-31.
 */

class DownloadFragment : Fragment() {

    companion object {
        private val instance by lazy {
            DownloadFragment()
        }

        fun instance() = instance
    }

}