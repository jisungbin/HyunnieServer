package com.sungbin.hyunnieserver.ui.folderdialog

import android.content.Context
import android.content.DialogInterface.*
import androidx.appcompat.app.AlertDialog
import com.sungbin.hyunnieserver.R

// from https://github.com/FirzenYogesh/FileListerDialog
class FolderDialog constructor(private var context: Context) {

    interface OnFolderSelectedListener {
        fun onFolderSelected(path: String)
    }

    private var alert = AlertDialog.Builder(context).create()
    private lateinit var folderDialogView: FolderDialogView
    private lateinit var onFolderSelectedListener: OnFolderSelectedListener

    init {
        init()
    }

    private fun init() {
        folderDialogView = FolderDialogView(context)
        alert.setView(folderDialogView)
        alert.setButton(
            BUTTON_POSITIVE,
            context.getString(R.string.choose)
        ) { dialogInterface, _ ->
            dialogInterface.dismiss()
            onFolderSelectedListener.onFolderSelected(folderDialogView.selected.absolutePath)
        }
        alert.setButton(
            BUTTON_NEGATIVE,
            context.getString(R.string.cancel)
        ) { dialogInterface, _ -> dialogInterface.dismiss() }
    }

    fun show() {
        alert.setTitle(context.getString(R.string.folderdialog_choose_folder))
        folderDialogView.start()
        alert.show()
        alert.getButton(BUTTON_NEUTRAL)
            .setOnClickListener { folderDialogView.goToDefaultDir() }
    }

    fun setOnFolderSelectedListener(onFolderSelectedListener: (String) -> Unit) {
        this.onFolderSelectedListener = object : OnFolderSelectedListener {
            override fun onFolderSelected(path: String) {
                onFolderSelectedListener(path)
            }
        }
    }

    companion object {
        fun createFolderDialog(context: Context): FolderDialog {
            return FolderDialog(context)
        }
    }

}
