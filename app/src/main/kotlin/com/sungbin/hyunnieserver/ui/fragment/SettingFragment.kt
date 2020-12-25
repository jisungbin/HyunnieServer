package com.sungbin.hyunnieserver.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sungbin.androidutils.util.DataUtil
import com.sungbin.androidutils.util.StorageUtil
import com.sungbin.androidutils.util.Util
import com.sungbin.hyunnieserver.R
import com.sungbin.hyunnieserver.databinding.FragmentSettingBinding
import com.sungbin.hyunnieserver.tool.manager.PathManager
import com.sungbin.hyunnieserver.ui.folderdialog.FolderDialog.Companion.createFolderDialog


/**
 * Created by SungBin on 2020-08-31.
 */

class SettingFragment : Fragment() {

    private val binding by lazy { FragmentSettingBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.tvVersion.text =
            getString(R.string.setting_version, Util.getAppVersionName(requireActivity()))
        binding.tvDownloadPath.text = DataUtil.readData(
            requireContext(),
            PathManager.DOWNLOAD_PATH,
            PathManager.DOWNLOAD_PATH_DEFAULT
        )
        binding.btnChangeDownloadPath.setOnClickListener {
            val fileDialog = createFolderDialog(requireContext())
            fileDialog.setOnFolderSelectedListener { _path ->
                val path = _path.replace(StorageUtil.sdcard, "")
                DataUtil.saveData(
                    requireContext(),
                    PathManager.DOWNLOAD_PATH,
                    path
                )
                binding.tvDownloadPath.text = path
            }
            fileDialog.show()
        }
    }

}