package com.sungbin.hyunnieserver.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.sungbin.hyunnieserver.ui.activity.MainActivity.Companion.backPressedAction
import com.sungbin.hyunnieserver.ui.activity.MainActivity.Companion.client
import com.sungbin.hyunnieserver.ui.activity.MainActivity.Companion.config
import com.sungbin.hyunnieserver.ui.activity.MainActivity.Companion.fileCache
import com.sungbin.hyunnieserver.ui.activity.MainActivity.Companion.fileList
import com.sungbin.hyunnieserver.ui.dialog.LoadingDialog
import org.apache.commons.io.output.CountingOutputStream
import org.apache.commons.net.ftp.FTPClient
import org.apache.commons.net.ftp.FTPFile
import org.jetbrains.anko.support.v4.runOnUiThread
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream


/**
 * Created by SungBin on 2020-08-31.
 */


class MainFragment : BaseFragment() {

    private val DEFAULT_PATH = "/메인 혀니서버/혀니서버"
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private var backPressedTime = 0L

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

        val id = config.getString("freeId")
        val password = config.getString("freePw")
        val address = /*config.getString("serverAddress")*/ "hn.osmg.kr"

        backPressedAction = { goBackPath() }

        binding.cvHome.setOnClickListener {
            fileList.postValue(fileCache[DEFAULT_PATH])
        }

        fileList.observe(viewLifecycleOwner, { files ->
            runOnUiThread {
                loadingDialog.close()
            }

            if (!files[0].isEmpty) {
                binding.fblEmptyFile.hide(true)
                binding.rvFile.apply {
                    show()
                    adapter = FileAdapter(files).apply {
                        setOnClickListener { file ->
                            if (file.path.contains(".")) {
                                ftpFileDownload(file)
                            } else {
                                changeFtpPath(file)
                            }
                        }
                    }
                }
            } else { // 파일 없음
                binding.rvFile.hide()
                binding.fblEmptyFile.show()
            }

            binding.rvPath.apply {
                adapter = PathAdapter(
                    fileList.value!![0].path.split("/")
                        .removePosition(ArrayPosition.FIRST)
                        .removePosition(ArrayPosition.LAST)
                ).apply {
                    setOnClickListener { path ->
                        var isCanAccess = false
                        fileCache.mapKeys { map ->
                            if (map.key.split("/").last() == path) {
                                isCanAccess = true
                                fileList.postValue(fileCache[map.key])
                            }
                        }
                        if (!isCanAccess) ToastUtil.show(
                            requireContext(),
                            context.getString(R.string.ftp_cant_go_path),
                            ToastLength.SHORT,
                            ToastType.WARNING
                        )
                    }
                }
            }.toBottomScroll()
        })

        if (fileList.value?.get(0)?.isEmpty == false) { // 이전 데이터 있을 때
            binding.fblEmptyFile.hide(true)
            binding.rvFile.apply {
                show()
                adapter = FileAdapter(fileList.value!!).apply {
                    setOnClickListener { file ->
                        if (file.path.contains(".")) {
                            ftpFileDownload(file)
                        } else {
                            changeFtpPath(file)
                        }
                    }
                }
            }
            backPressedAction = { goBackPath() }
        } else { // 최초 실행
            Thread {
                try {
                    runOnUiThread {
                        loadingDialog.show()
                    }

                    client.connect(address)
                    client.login(id, password)
                    client.enterLocalPassiveMode() // required for connection

                    val list = ArrayList<com.sungbin.hyunnieserver.model.File>()
                    (client.listFiles(DEFAULT_PATH) as Array<FTPFile>).map {
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
                                "$DEFAULT_PATH/${it.name}",
                                FileUtil.getType(it.name, it.size),
                                FileUtil.getLastModifyTime(it.timestamp),
                                false
                            )
                        )
                    }
                    fileCache[DEFAULT_PATH] = list
                    fileList.postValue(list)
                } catch (exception: Exception) {
                    ExceptionUtil.except(exception, requireContext())
                }
            }.start()
        }
    }

    private fun goBackPath() {
        if (fileList.value!![0].path.replace(DEFAULT_PATH, "")
                .split("/").size > 2
        ) {
            goFtpBackPath(fileList.value!![0].path)
        } else {
            cantGoBack()
        }
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
        val cache = fileCache[file.path]
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
                fileCache[file.path] = list
                fileList.postValue(list)
            }.start()
        } else {
            fileList.postValue(cache)
        }
    }

    private fun goFtpBackPath(path: String) {
        val levelOneLastPathName = path.split("/").last()
        val levelOneBackPath = path.replaceLast("/$levelOneLastPathName", "")
        val levelTwoLastPathName = levelOneBackPath.split("/").last()
        val levelTwoBackPath = levelOneBackPath.replaceLast("/$levelTwoLastPathName", "")

        val backCache = fileCache[levelTwoBackPath]
        loadingDialog.show()
        backCache?.let {
            fileList.postValue(it)
        } ?: cantGoBack()
    }

    private fun cantGoBack() {
        if (System.currentTimeMillis() > backPressedTime + 2000) {
            ToastUtil.show(
                requireContext(),
                getString(R.string.ftp_cant_go_back),
                ToastLength.SHORT,
                ToastType.WARNING
            )
            backPressedTime = System.currentTimeMillis()
        } else if (System.currentTimeMillis() <= backPressedTime + 2000) {
            requireActivity().finishAfterTransition()
        }
    }

}