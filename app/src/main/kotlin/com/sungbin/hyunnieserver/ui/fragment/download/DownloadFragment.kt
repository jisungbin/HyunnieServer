package com.sungbin.hyunnieserver.ui.fragment.download

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sungbin.hyunnieserver.databinding.TestFragmentBinding
import com.sungbin.hyunnieserver.ui.fragment.BaseFragment


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
        binding.tvTest.text = "DownloadFragment"
    }

}