package com.sungbin.hyunnieserver.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sungbin.androidutils.extensions.doDelay
import com.sungbin.androidutils.extensions.hide
import com.sungbin.androidutils.extensions.show
import com.sungbin.androidutils.util.DataUtil
import com.sungbin.androidutils.util.StorageUtil
import com.sungbin.hyunnieserver.R
import com.sungbin.hyunnieserver.adapter.FileAdapter
import com.sungbin.hyunnieserver.databinding.FragmentDownloadBinding
import com.sungbin.hyunnieserver.model.File
import com.sungbin.hyunnieserver.tool.manager.PathManager
import com.sungbin.hyunnieserver.tool.util.FileUtil
import java.util.*
import kotlin.collections.ArrayList


/**
 * Created by SungBin on 2020-08-31.
 */

class DownloadFragment : Fragment() {

    private val binding by lazy { FragmentDownloadBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        init()

        binding.srlContainer.setColorSchemeResources(R.color.colorPrimary)
        binding.srlContainer.setOnRefreshListener {
            doDelay(1000) {
                init()
                binding.srlContainer.isRefreshing = false
            }
        }
    }

    private fun init() {
        val downloadFile = java.io.File(
            "${StorageUtil.sdcard}/${
                DataUtil.readData(
                    requireContext(),
                    PathManager.DOWNLOAD_PATH,
                    PathManager.DOWNLOAD_PATH_DEFAULT
                )
            }"
        )

        val files = ArrayList<File>()
        downloadFile.listFiles()?.map {
            val lastModifyDate = Date(it.lastModified())
            val lastModifyCalendar = Calendar.getInstance().apply { time = lastModifyDate }
            val file = File(
                it.name,
                StorageUtil.getSize(it.length()),
                it.length(),
                it.path,
                FileUtil.getType(it.name, it.length()),
                FileUtil.getLastModifyTime(lastModifyCalendar),
                false
            )
            files.add(file)
        }

        if (files.isEmpty()) {
            binding.rvFile.hide(true)
            binding.fblEmptyFile.show()
        } else {
            binding.rvFile.adapter = FileAdapter(files)
        }
    }

}