package com.sungbin.hyunnieserver.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sungbin.hyunnieserver.databinding.FragmentDownloadBinding


/**
 * Created by SungBin on 2020-08-31.
 */

class DownloadFragment : Fragment() {

    private val binding by lazy { FragmentDownloadBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root

}