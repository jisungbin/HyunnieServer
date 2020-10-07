package com.sungbin.hyunnieserver.ui.fragment.download

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.sungbin.hyunnieserver.R
import com.sungbin.hyunnieserver.ui.fragment.BaseFragment
import kotlinx.android.synthetic.main.test_fragment.*


/**
 * Created by SungBin on 2020-08-31.
 */

class DownloadFragment : BaseFragment() {

    companion object {
        private lateinit var downloadFragment: DownloadFragment

        fun instance(): DownloadFragment {
            if (!::downloadFragment.isInitialized) {
                downloadFragment = DownloadFragment()
            }
            return downloadFragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = inflater.inflate(R.layout.test_fragment, container, false)!!

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        tv_test.text = "DownloadFragment"
    }

}