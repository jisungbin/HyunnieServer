package com.sungbin.hyunnieserver.ui.fragment.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.sungbin.hyunnieserver.R
import com.sungbin.hyunnieserver.tool.manager.PathManager
import com.sungbin.sungbintool.DataUtils
import com.sungbin.sungbintool.LogUtils
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.WithFragmentBindings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = inflater.inflate(R.layout.fragment_main, container, false)!!

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        retainInstance = false

        viewModel.fileList.observe(viewLifecycleOwner, {
            // todo: 파일 갱신 옵저버
        })

        val context = requireContext()
        val address = DataUtils.readData(context, PathManager.SERVER_ADDRESS, "")
        val id = DataUtils.readData(context, PathManager.ID, "")
        val password = DataUtils.readData(context, PathManager.PASSWORD, "")

        LogUtils.w(arrayOf(address, id, password))

        client.connect(address)
        client.login(id, password)
        client.enterLocalPassiveMode()
        CoroutineScope(Dispatchers.Main).launch {
            val list = withContext(Dispatchers.Default) {
                client.listFiles("/메인 혀니서버/혀니서버") as Array<FTPFile>
            }
            list.map {
                LogUtils.w(it)
            }
        }
        /*LogUtils.w("5", (client.listFiles("/메인 혀니서버") as Array<FTPFile>).size)
        *//*ftpFiles.
        for (element in ftpFiles) {
            val size = when (element.isFile) {
                true -> {
                    StorageUtils.getSize(element.size)**
                }
                else -> element.
            }
            items.add(FileListItem(element.isFile, element.name, count))
        }*/
    }

}