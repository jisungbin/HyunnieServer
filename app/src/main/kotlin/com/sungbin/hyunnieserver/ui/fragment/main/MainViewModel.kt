package com.sungbin.hyunnieserver.ui.fragment.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sungbin.hyunnieserver.model.FileItem


/**
 * Created by SungBin on 2020-08-31.
 */

class MainViewModel : ViewModel() {

    val fileList: MutableLiveData<List<FileItem>> = MutableLiveData()
    val fileCache = HashMap<String, List<FileItem>>()

}