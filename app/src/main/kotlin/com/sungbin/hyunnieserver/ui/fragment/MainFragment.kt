package com.sungbin.hyunnieserver.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.asLiveData
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
import com.sungbin.hyunnieserver.datastore.DataManager
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
import com.sungbin.hyunnieserver.ui.dialog.SortDialog
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


// todo: viewmodel로 해야하는데!!!!!!!!!!!
class MainFragment : Fragment() {

    private val DEFAULT_PATH = "/메인 혀니서버/혀니서버"
    private val binding by lazy { FragmentMainBinding.inflate(layoutInflater) }
    private var backPressedTime = 0L

    private val sortDialog by lazy { SortDialog.instance() } // 2중 싱글톤???
    private val loadingDialog by lazy { LoadingDialog(requireActivity()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val id = config.getString("freeId")
        val password = config.getString("freePw")
        val address = config.getString("serverAddress")

        backPressedAction = { goBackPath() }

        binding.cvHome.setOnClickListener { fileList.postValue(fileCache[DEFAULT_PATH]) }
        binding.ivSort.setOnClickListener { sortDialog.show(parentFragmentManager, "") }

        fileList.observe(viewLifecycleOwner) { files ->
            runOnUiThread { loadingDialog.close() }

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
        }

        if (fileList.value?.get(0)?.isEmpty == false) { // 이전 데이터 있을 때
            binding.fblEmptyFile.hide(true)
            binding.rvFile.apply {
                show()
                adapter = FileAdapter(fileList.value!!).apply {
                    setOnClickListener { file ->
                        if (file.isFile) {
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
                    runOnUiThread { loadingDialog.show() }

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
                                false,
                                it.isFile
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

        DataManager.sortTypeFlow.asLiveData().observe(viewLifecycleOwner) {
            (binding.rvFile.adapter as? FileAdapter)?.sort(it)
        }

        DataManager.sortNameFlow.asLiveData().observe(viewLifecycleOwner) {
            (binding.rvFile.adapter as? FileAdapter)?.sort(it)
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
        var prePercent = 0
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
                    if (percent > prePercent) {
                        notification.setContentTitle(
                            getString(
                                R.string.notification_downloading_percent,
                                percent
                            )
                        )
                        notification.setProgress(100, percent, false)
                        manager.notify(notificationId, notification.build())
                        prePercent = percent
                    }
                }
            }

            client.bufferSize = 2048 * 2048
            client.retrieveFile(file.path, cos)
        } catch (exception: Exception) {
            ExceptionUtil.except(exception, requireContext())
            ToastUtil.show(
                requireContext(),
                getString(R.string.main_download_fail, file.name),
                ToastLength.SHORT,
                ToastType.ERROR
            )
            NotificationUtil.remove(requireContext(), notificationId)
        } finally {
            outputStream?.close()
            ToastUtil.show(
                requireContext(),
                getString(R.string.main_download_done, file.name),
                ToastLength.SHORT,
                ToastType.SUCCESS
            )
            NotificationUtil.remove(requireContext(), notificationId)
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
                            false,
                            it.isFile
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
                            true,
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