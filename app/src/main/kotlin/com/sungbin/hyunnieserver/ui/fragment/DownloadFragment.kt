package com.sungbin.hyunnieserver.ui.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sungbin.androidutils.extensions.doDelay
import com.sungbin.androidutils.extensions.hide
import com.sungbin.androidutils.extensions.show
import com.sungbin.androidutils.util.*
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
            doDelay(500) {
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

        var totalSize = 0L
        files.map {
            totalSize += it.originSize
        }

        if (files.isEmpty()) {
            binding.rvFile.hide(true)
            binding.fblEmptyFile.show()
            binding.tvDownloadSize.text =
                getString(R.string.download_downloaded_size, "0 B")
            binding.ivClear.hide(true)
        } else {
            binding.fblEmptyFile.hide(true)
            binding.rvFile.show()
            binding.rvFile.adapter = FileAdapter(files)
            binding.tvDownloadSize.text =
                getString(R.string.download_downloaded_size, StorageUtil.getSize(totalSize))
            binding.ivClear.show()
            binding.ivClear.setOnClickListener {
                val dialog = AlertDialog.Builder(requireActivity())
                dialog.setTitle(getString(R.string.download_delete_all))
                dialog.setMessage(getString(R.string.download_confirm_delete))
                dialog.setPositiveButton(getString(R.string.delete)) { _, _ ->
                    StorageUtil.deleteAll(
                        "${StorageUtil.sdcard}/${
                            DataUtil.readData(
                                requireContext(),
                                PathManager.DOWNLOAD_PATH,
                                PathManager.DOWNLOAD_PATH_DEFAULT
                            )
                        }"
                    )
                    ToastUtil.show(
                        requireContext(), getString(R.string.download_all_file_deleted),
                        ToastLength.SHORT, ToastType.SUCCESS
                    )
                    init()
                }
                dialog.setNegativeButton(getString(R.string.cancel), null)
                dialog.show()
            }
        }
    }

}