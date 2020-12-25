package com.sungbin.hyunnieserver.ui.folderdialog

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File

// from https://github.com/FirzenYogesh/FileListerDialog
class FolderDialogView : RecyclerView {

    private var adapter: FolderDialogAdapter? = null

    val selected: File
        get() = adapter!!.selected

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        init()
    }

    private fun init() {
        layoutManager = LinearLayoutManager(context, VERTICAL, false)
        adapter = FolderDialogAdapter(this)
    }

    fun start() {
        setAdapter(adapter)
        adapter!!.start()
    }

    fun goToDefaultDir() {
        adapter!!.goToDefault()
    }
}
