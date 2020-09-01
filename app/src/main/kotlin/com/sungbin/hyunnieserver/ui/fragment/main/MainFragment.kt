package com.sungbin.hyunnieserver.ui.fragment.main

import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.sungbin.hyunnieserver.R
import com.sungbin.hyunnieserver.adapter.FileAdapter
import com.sungbin.hyunnieserver.model.FileItem
import com.sungbin.hyunnieserver.tool.manager.PathManager
import com.sungbin.hyunnieserver.tool.ui.NotificationUtil
import com.sungbin.hyunnieserver.tool.util.FileUtil
import com.sungbin.hyunnieserver.tool.util.OnBackPressedUtil
import com.sungbin.hyunnieserver.ui.dialog.LoadingDialog
import com.sungbin.sungbintool.DataUtils
import com.sungbin.sungbintool.LogUtils
import com.sungbin.sungbintool.StorageUtils
import com.sungbin.sungbintool.StorageUtils.sdcard
import com.sungbin.sungbintool.ToastUtils
import com.sungbin.sungbintool.extensions.replaceLast
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.WithFragmentBindings
import kotlinx.android.synthetic.main.fragment_main.*
import org.apache.commons.io.output.CountingOutputStream
import org.apache.commons.net.ftp.FTPClient
import org.apache.commons.net.ftp.FTPFile
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import javax.inject.Inject


/**
 * Created by SungBin on 2020-08-31.
 */

@AndroidEntryPoint
@WithFragmentBindings
class MainFragment : Fragment(), OnBackPressedUtil {

    companion object {
        private val instance by lazy {
            MainFragment()
        }

        fun instance() = instance
    }

    @Inject
    lateinit var client: FTPClient

    private val viewModel by viewModels<MainViewModel>()

    private val loadingDialog by lazy {
        LoadingDialog(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = inflater.inflate(R.layout.fragment_main, container, false)!!

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        retainInstance = false

        viewModel.fileList.observe(viewLifecycleOwner, {
            rv_file.adapter = FileAdapter(it, requireActivity()).apply {
                setOnClickListener { file ->
                    if (file.path.contains(".")) {
                        ftpFileDownload(file)
                    } else {
                        changeFtpPath(file)
                    }
                }
            }
        })

        val context = requireContext()
        val address = DataUtils.readData(context, PathManager.SERVER_ADDRESS, "")
        val id = DataUtils.readData(context, PathManager.ID, "")
        val password = DataUtils.readData(context, PathManager.PASSWORD, "")

        client.connect(address) // client init
        client.login(id, password)
        client.enterLocalPassiveMode() // required for connection
        loadingDialog.show()

        val list = ArrayList<FileItem>()
        (client.listFiles("/메인 혀니서버/혀니서버") as Array<FTPFile>).map {
            val size = if (it.isFile) {
                StorageUtils.getSize(it.size)
            } else {
                ""
            }
            list.add(
                FileItem(
                    it.name,
                    size,
                    it.size,
                    "/메인 혀니서버/혀니서버/${it.name}",
                    FileUtil.getType(it.name, it.size),
                    FileUtil.getLastModifyTime(it.timestamp)
                )
            )
        }
        viewModel.fileCache["/메인 혀니서버/혀니서버"] = list
        viewModel.fileList.postValue(list)
        loadingDialog.close()
    }

    private fun ftpFileDownload(file: FileItem) {
        NotificationUtil.createChannel(requireContext())
        client.setFileType(FTPClient.BINARY_FILE_TYPE)
        val notificationId = 1000
        var outputStream: OutputStream? = null
        val manager = NotificationUtil.getManager(requireContext())
        val notification = NotificationUtil.getDownloadNotification(
            requireContext(), notificationId, getString(
                R.string.notification_downloading
            ), file.name
        )

        try {
            manager.notify(notificationId, notification.build())
            val downloadFile = File(
                "$sdcard/${
                    DataUtils.readData(
                        requireContext(),
                        PathManager.DOWNLOAD_PATH,
                        PathManager.DOWNLOAD_PATH_DEFAULT
                    )
                }/${file.name}"
            )
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
        } catch (e: Exception) {
            LogUtils.w(e)
        } finally {
            outputStream?.close()
        }
    }

    private fun changeFtpPath(file: FileItem) {
        loadingDialog.show()

        val cache = viewModel.fileCache[file.path]
        if (cache == null) {
            val list = ArrayList<FileItem>()
            (client.listFiles(file.path) as Array<FTPFile>).map {
                val size = if (it.isFile) {
                    StorageUtils.getSize(it.size)
                } else {
                    ""
                }
                list.add(
                    FileItem(
                        it.name,
                        size,
                        it.size,
                        "${file.path}/${it.name}",
                        FileUtil.getType(it.name, it.size),
                        FileUtil.getLastModifyTime(it.timestamp)
                    )
                )
            }
            viewModel.fileCache[file.path] = list
            viewModel.fileList.postValue(list)
        } else {
            viewModel.fileList.postValue(cache)
        }
        loadingDialog.close()
    }

    private fun changeBackPath(file: FileItem) {

        fun cantGoBack() {
            ToastUtils.show(
                requireContext(),
                getString(R.string.ftp_cant_go_back),
                ToastUtils.SHORT,
                ToastUtils.WARNING
            )
        }

        val levelOneLastPathName = file.path.split("/").last()
        val levelOneBackPath = file.path.replaceLast("/$levelOneLastPathName", "")
        val levelTwoLastPathName = levelOneBackPath.split("/").last()
        val levelTwoBackPath = levelOneBackPath.replaceLast("/$levelTwoLastPathName", "")

        val backCache = viewModel.fileCache[levelTwoBackPath]
        loadingDialog.show()
        backCache?.let {
            viewModel.fileList.postValue(it)
        } ?: cantGoBack()
        loadingDialog.close()
    }

    override fun onBackPressed(activity: Activity): Boolean {
        return when (viewModel.fileList.value!![0].path.replace("/메인 혀니서버/혀니서버", "")
            .split("/").size > 2) {
            true -> {
                changeBackPath(viewModel.fileList.value!![0])
                false
            }
            else -> {
                AlertDialog.Builder(activity).run {
                    setTitle(getString(R.string.close))
                    setMessage(getString(R.string.main_really_close))
                    setNeutralButton("좀 더 있기") { _, _ -> }
                    setPositiveButton("종료하기") { _, _ ->
                        activity.finish()
                    }
                    show()
                    true
                }
            }
        }
    }

}