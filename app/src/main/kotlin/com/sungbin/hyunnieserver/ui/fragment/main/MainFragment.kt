package com.sungbin.hyunnieserver.ui.fragment.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.sungbin.androidutils.extensions.hide
import com.sungbin.androidutils.extensions.replaceLast
import com.sungbin.androidutils.extensions.show
import com.sungbin.androidutils.extensions.toBottomScroll
import com.sungbin.androidutils.util.*
import com.sungbin.androidutils.util.StorageUtil.sdcard
import com.sungbin.hyunnieserver.R
import com.sungbin.hyunnieserver.adapter.FileAdapter
import com.sungbin.hyunnieserver.adapter.PathAdapter
import com.sungbin.hyunnieserver.databinding.FragmentMainBinding
import com.sungbin.hyunnieserver.model.FileType
import com.sungbin.hyunnieserver.tool.manager.PathManager
import com.sungbin.hyunnieserver.tool.ui.NotificationUtil
import com.sungbin.hyunnieserver.tool.util.ArrayPosition
import com.sungbin.hyunnieserver.tool.util.ExceptionUtil
import com.sungbin.hyunnieserver.tool.util.FileUtil
import com.sungbin.hyunnieserver.tool.util.removePosition
import com.sungbin.hyunnieserver.ui.dialog.LoadingDialog
import com.sungbin.hyunnieserver.ui.fragment.BaseFragment
import org.apache.commons.io.output.CountingOutputStream
import org.apache.commons.net.ftp.FTPClient
import org.apache.commons.net.ftp.FTPFile
import org.jetbrains.anko.support.v4.runOnUiThread
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import kotlin.collections.set


/**
 * Created by SungBin on 2020-08-31.
 */


class MainFragment : BaseFragment() {

    companion object {
        private lateinit var mainFragment: MainFragment

        fun instance(): MainFragment {
            if (!::mainFragment.isInitialized) {
                mainFragment = MainFragment()
            }
            return mainFragment
        }
    }

    private val viewModel by viewModels<MainViewModel>()
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private val loadingDialog by lazy {
        LoadingDialog(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val config = Firebase.remoteConfig
        val id = config.getString("freeId")
        val password = config.getString("freePw")
        val address = "hn.osmg.kr"

        binding.cvHome.setOnClickListener {
            viewModel.fileList.postValue(viewModel.fileCache["/메인 혀니서버/혀니서버"])
        }

        binding.cvBack.setOnClickListener {
            if (viewModel.fileList.value!![0].path.replace("/메인 혀니서버/혀니서버", "")
                    .split("/").size > 2
            ) {
                goFtpBackPath(viewModel.fileList.value!![0].path)
            } else {
                cantGoBack()
            }
        }

        viewModel.fileList.observe(viewLifecycleOwner, {
            runOnUiThread {
                loadingDialog.close()
            }

            if (!it[0].isEmpty) {
                binding.fblEmptyFile.hide(true)
                binding.rvFile.apply {
                    show()
                    adapter = FileAdapter(it, requireActivity()).apply {
                        setOnClickListener { file ->
                            if (file.path.contains(".")) {
                                ftpFileDownload(file)
                            } else {
                                changeFtpPath(file)
                            }
                        }
                    }
                }
                binding.rvPath.apply {
                    adapter = PathAdapter(
                        viewModel.fileList.value!![0].path.split("/")
                            .removePosition(ArrayPosition.FIRST)
                            .removePosition(ArrayPosition.LAST), requireActivity()
                    ).apply {
                        setOnClickListener { path ->
                            viewModel.fileCache.mapKeys { map ->
                                if (map.key.split("/").last() == path) {
                                    viewModel.fileList.postValue(viewModel.fileCache[map.key])
                                }
                            }
                        }
                    }
                }.toBottomScroll()
            } else { // 파일 없음
                binding.rvFile.hide()
                binding.fblEmptyFile.show()
                binding.rvPath.apply {
                    adapter = PathAdapter(
                        viewModel.fileList.value!![0].path.split("/")
                            .removePosition(ArrayPosition.FIRST)
                            .removePosition(ArrayPosition.LAST), requireActivity()
                    ).apply {
                        setOnClickListener { path ->
                            var isCanAccess = false
                            viewModel.fileCache.mapKeys { map ->
                                if (map.key.split("/").last() == path) {
                                    isCanAccess = true
                                    viewModel.fileList.postValue(viewModel.fileCache[map.key])
                                }
                            }
                            if (!isCanAccess) ToastUtil.show(
                                requireContext(),
                                context.getString(R.string.main_cant_go_path),
                                ToastLength.SHORT,
                                ToastType.WARNING
                            )
                        }
                    }
                }.toBottomScroll()
            }
        })

        Thread {
            try {
                runOnUiThread {
                    loadingDialog.show()
                }

                client.connect(address) // client inits
                client.login(id, password)
                client.enterLocalPassiveMode() // required for connection

                val list = ArrayList<com.sungbin.hyunnieserver.model.File>()
                (client.listFiles("/메인 혀니서버/혀니서버") as Array<FTPFile>).map {
                    val size = if (it.isFile) {
                        StorageUtil.getSize(it.size)
                    } else {
                        ""
                    }
                    list.add(
                        com.sungbin.hyunnieserver.model.File(
                            it.name,
                            size,
                            it.size,
                            "/메인 혀니서버/혀니서버/${it.name}",
                            FileUtil.getType(it.name, it.size),
                            FileUtil.getLastModifyTime(it.timestamp),
                            false
                        )
                    )
                }
                viewModel.fileCache["/메인 혀니서버/혀니서버"] = list
                viewModel.fileList.postValue(list)
            } catch (exception: Exception) {
                ExceptionUtil.except(exception, requireContext())
            }
        }.start()
    }

    private fun ftpFileDownload(file: com.sungbin.hyunnieserver.model.File) {
        client.setFileType(FTPClient.BINARY_FILE_TYPE)
        val notificationId = 1000
        var outputStream: OutputStream? = null
        val manager = NotificationUtil.getManager(requireContext())
        val notification = NotificationUtil.getDownloadNotification(
            requireContext(), notificationId, getString(
                R.string.notification_downloading
            ), file.name
        )
        manager.notify(notificationId, notification.build())

        try {
            val downloadFile = File(
                "$sdcard/${
                    DataUtil.readData(
                        requireContext(),
                        PathManager.DOWNLOAD_PATH,
                        PathManager.DOWNLOAD_PATH_DEFAULT
                    )
                }/${file.name}"
            )
            File(downloadFile.parent ?: return).mkdirs()
            downloadFile.createNewFile()

            outputStream = BufferedOutputStream(FileOutputStream(downloadFile))

            val cos = object : CountingOutputStream(outputStream) {
                override fun afterWrite(n: Int) {
                    super.afterWrite(n)
                    val percent = (count * 100 / file.originSize).toInt()
                    notification.setProgress(100, percent, false)
                }
            }

            client.bufferSize = 2048 * 2048
            client.retrieveFile(file.path, cos)
        } catch (exception: Exception) {
            ExceptionUtil.except(exception, requireContext())
        } finally {
            outputStream?.close()
        }
    }

    private fun changeFtpPath(file: com.sungbin.hyunnieserver.model.File) {
        val cache = viewModel.fileCache[file.path]
        if (cache == null) {
            Thread {
                runOnUiThread {
                    loadingDialog.show()
                }

                val list = ArrayList<com.sungbin.hyunnieserver.model.File>()
                (client.listFiles(file.path) as Array<FTPFile>).map {
                    val size = if (it.isFile) {
                        StorageUtil.getSize(it.size)
                    } else {
                        ""
                    }
                    list.add(
                        com.sungbin.hyunnieserver.model.File(
                            it.name,
                            size,
                            it.size,
                            "${file.path}/${it.name}",
                            FileUtil.getType(it.name, it.size),
                            FileUtil.getLastModifyTime(it.timestamp),
                            false
                        )
                    )
                }
                if (list.isEmpty()) {
                    list.add(
                        com.sungbin.hyunnieserver.model.File(
                            "empty file",
                            "0B",
                            0L,
                            "${file.path}/empty file",
                            FileType.EMPTY,
                            "",
                            true
                        )
                    )
                }
                viewModel.fileCache[file.path] = list
                viewModel.fileList.postValue(list)
            }.start()
        } else {
            viewModel.fileList.postValue(cache)
        }
    }

    private fun goFtpBackPath(path: String) {
        val levelOneLastPathName = path.split("/").last()
        val levelOneBackPath = path.replaceLast("/$levelOneLastPathName", "")
        val levelTwoLastPathName = levelOneBackPath.split("/").last()
        val levelTwoBackPath = levelOneBackPath.replaceLast("/$levelTwoLastPathName", "")

        val backCache = viewModel.fileCache[levelTwoBackPath]
        loadingDialog.show()
        backCache?.let {
            viewModel.fileList.postValue(it)
        } ?: cantGoBack()
        loadingDialog.close()
    }

    private fun cantGoBack() {
        ToastUtil.show(
            requireContext(),
            getString(R.string.ftp_cant_go_back),
            ToastLength.SHORT,
            ToastType.WARNING
        )
    }

}