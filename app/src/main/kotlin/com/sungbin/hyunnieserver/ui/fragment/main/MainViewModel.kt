package com.sungbin.hyunnieserver.ui.fragment.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sungbin.hyunnieserver.model.File


/**
 * Created by SungBin on 2020-08-31.
 */

class MainViewModel : ViewModel() {

    val fileList: MutableLiveData<List<File>> = MutableLiveData()
    val fileCache = HashMap<String, List<File>>()

}