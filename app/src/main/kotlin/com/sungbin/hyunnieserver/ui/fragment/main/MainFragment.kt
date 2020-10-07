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
import com.sungbin.hyunnieserver.tool.manager.PathManager
import com.sungbin.hyunnieserver.tool.ui.NotificationUtil
import com.sungbin.hyunnieserver.tool.util.ExceptionUtil
import com.sungbin.hyunnieserver.tool.util.FileUtil
import com.sungbin.hyunnieserver.tool.util.OnBackPressedUtil
import com.sungbin.hyunnieserver.ui.dialog.LoadingDialog
import com.sungbin.sungbintool.extensions.replaceLast
import com.sungbin.sungbintool.util.*
import com.sungbin.sungbintool.util.StorageUtil.sdcard
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
        private lateinit var mainFragment: MainFragment

        fun instance(): MainFragment {
            if (!::mainFragment.isInitialized) {
                mainFragment = MainFragment()
            }
            return mainFragment
        }
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
        val address = DataUtil.readData(context, PathManager.SERVER_ADDRESS, "")
        val id = DataUtil.readData(context, PathManager.ID, "")
        val password = DataUtil.readData(context, PathManager.PASSWORD, "")

        client.connect(address) // client init
        client.login(id, password)
        client.enterLocalPassiveMode() // required for connection
        loadingDialog.show()

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
                    FileUtil.getLastModifyTime(it.timestamp)
                )
            )
        }
        viewModel.fileCache["/메인 혀니서버/혀니서버"] = list
        viewModel.fileList.postValue(list)
        loadingDialog.close()
    }

    private fun ftpFileDownload(file: com.sungbin.hyunnieserver.model.File) {
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
        loadingDialog.show()

        val cache = viewModel.fileCache[file.path]
        if (cache == null) {
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

    private fun changeBackPath(file: com.sungbin.hyunnieserver.model.File) {

        fun cantGoBack() {
            ToastUtil.show(
                requireContext(),
                getString(R.string.ftp_cant_go_back),
                ToastLength.SHORT,
                ToastType.WARNING
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
                    setNeutralButton(getString(R.string.main_stay)) { _, _ -> }
                    setPositiveButton(getString(R.string.main_finish)) { _, _ ->
                        activity.finish()
                    }
                    show()
                    true
                }
            }
        }
    }

}