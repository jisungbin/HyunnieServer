package com.sungbin.hyunnieserver.ui.fragment.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.sungbin.hyunnieserver.R
import com.sungbin.hyunnieserver.adapter.FileAdapter
import com.sungbin.hyunnieserver.model.FileItem
import com.sungbin.hyunnieserver.tool.manager.PathManager
import com.sungbin.hyunnieserver.tool.util.FileUtil
import com.sungbin.hyunnieserver.ui.dialog.LoadingDialog
import com.sungbin.sungbintool.DataUtils
import com.sungbin.sungbintool.StorageUtils
import com.sungbin.sungbintool.ToastUtils
import com.sungbin.sungbintool.extensions.replaceLast
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.WithFragmentBindings
import kotlinx.android.synthetic.main.fragment_main.*
import org.apache.commons.net.ftp.FTPClient
import org.apache.commons.net.ftp.FTPFile
import javax.inject.Inject


/**
 * Created by SungBin on 2020-08-31.
 */

@AndroidEntryPoint
@WithFragmentBindings
class MainFragment : Fragment() {

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
                    changeFtpPath(file)
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
                    "/메인 혀니서버/혀니서버/${it.name}",
                    FileUtil.getType(it.name, it.size),
                    FileUtil.getLastModifyTime(it.timestamp)
                )
            )
        }
        viewModel.fileList.postValue(list)
        loadingDialog.close()
    }

    private fun changeFtpPath(item: FileItem) {
        loadingDialog.show()

        val list = ArrayList<FileItem>()
        (client.listFiles(item.path) as Array<FTPFile>).map {
            val size = if (it.isFile) {
                StorageUtils.getSize(it.size)
            } else {
                ""
            }
            list.add(
                FileItem(
                    it.name,
                    size,
                    "${item.path}/${it.name}",
                    FileUtil.getType(it.name, it.size),
                    FileUtil.getLastModifyTime(it.timestamp)
                )
            )
        }
        viewModel.fileList.postValue(list)
        loadingDialog.close()
    }

    private fun changeBackPath(item: FileItem) {

        fun cantGoBack() {
            ToastUtils.show(
                requireContext(),
                getString(R.string.ftp_cant_go_back),
                ToastUtils.SHORT,
                ToastUtils.WARNING
            )
        }

        val lastPathName = item.path.split("/").last()
        val backPath = item.path.replaceLast("/$lastPathName", "")
        val backCache = viewModel.fileCache[backPath]

        loadingDialog.show()

        val list = ArrayList<FileItem>()
        backCache?.map {
            list.add(it)
        } ?: cantGoBack()
        viewModel.fileList.postValue(list)
        loadingDialog.close()
    }

}