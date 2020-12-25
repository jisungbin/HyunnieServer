package com.sungbin.hyunnieserver.ui.folderdialog

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.sungbin.androidutils.extensions.get
import com.sungbin.androidutils.util.StorageUtil
import com.sungbin.androidutils.util.ToastLength
import com.sungbin.androidutils.util.ToastType
import com.sungbin.androidutils.util.ToastUtil
import com.sungbin.hyunnieserver.R
import java.io.File
import java.util.*

/**
 * Created by root on 9/7/17.
 */

// from https://github.com/FirzenYogesh/FileListerDialog
class FolderDialogAdapter constructor(private val view: FolderDialogView) :
    RecyclerView.Adapter<FolderDialogAdapter.FolderListViewHolder>() {

    private var folderList = ArrayList<File>()
    private var context = view.context
    private var unreadableDir = false

    var selected = File(StorageUtil.sdcard)

    fun start() {
        folderListener(File(StorageUtil.sdcard))
    }

    private fun folderListener(dir: File) {
        val folderList = ArrayList<File>()
        if (dir.absolutePath == "/"
            || dir.absolutePath == "/storage"
            || dir.absolutePath == "/storage/emulated"
            || dir.absolutePath == "/mnt"
        ) {
            unreadableDir = true
            val vols = context.getExternalFilesDirs(null)
            if (vols != null && vols.isNotEmpty()) {
                for (file in vols) {
                    if (file != null) {
                        var path = file.absolutePath
                        path =
                            path.replace("/Android/data/([a-zA-Z_][.\\w]*)/files".toRegex(), "")
                        folderList.add(File(path))
                    }
                }
            } else {
                folderList.add(File(StorageUtil.sdcard))
            }
        } else {
            unreadableDir = false
            val files = dir.listFiles()
            files?.forEach { file ->
                if (file.isDirectory) folderList.add(file)
            }
        }
        this.folderList = ArrayList(folderList)
        this.folderList.sortWith { folder, folder2 ->
            folder.name.compareTo(folder2.name)
        }
        selected = dir
        if (dir.absolutePath != "/") {
            dirUp()
        }
        notifyDataSetChanged()
        view.scrollToPosition(0)
    }

    private fun dirUp() {
        if (!unreadableDir) {
            folderList.add(0, selected.parentFile!!)
            folderList.add(1, File(""))
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        FolderListViewHolder(View.inflate(context, R.layout.layout_folderdialog_item, null))

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(viewHolder: FolderListViewHolder, position: Int) {
        val folder = folderList[position]
        viewHolder.name.text = folder.name
        if (unreadableDir) {
            if (position == 0) {
                viewHolder.name.text = "${folder.name} (내부)"
            } else {
                viewHolder.name.text = "${folder.name} (외부)"
            }
        }
        if (position == 0 && !unreadableDir) {
            viewHolder.icon.setImageResource(R.drawable.ic_baseline_arrow_back_24)
        } else {
            if (folder.path.isEmpty()) viewHolder.icon.setImageResource(R.drawable.ic_baseline_create_new_folder_24)
        }
    }

    fun goToDefault() {
        folderListener(File(StorageUtil.sdcard))
    }

    inner class FolderListViewHolder(view: View) : RecyclerView.ViewHolder(view),
        View.OnClickListener {

        val name = view[R.id.tv_name, TextView::class.java]
        val icon = view[R.id.iv_icon, ImageView::class.java]

        init {
            view[R.id.ll_main_container, LinearLayout::class.java].setOnClickListener(this)
        }

        override fun onClick(v: View) {
            if (folderList[adapterPosition].path.isEmpty()) {
                val view = View.inflate(context, R.layout.layout_create_folder, null)
                val editText = view.findViewById<EditText>(R.id.et_folder_name)
                val builder = AlertDialog.Builder(context)
                    .setView(view)
                    .setPositiveButton(context.getString(R.string.done)) { _, _ -> }
                val dialog = builder.create()
                dialog.show()
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                    val name = editText.text!!.toString()
                    if (name.isBlank()) {
                        ToastUtil.show(
                            context,
                            context.getString(R.string.folderdialog_folder_name_empty),
                            ToastLength.SHORT,
                            ToastType.WARNING
                        )
                    } else {
                        val file = File(selected, name)
                        if (file.exists()) {
                            ToastUtil.show(
                                context,
                                context.getString(R.string.folderdialog_exist_folder_name),
                                ToastLength.SHORT,
                                ToastType.WARNING
                            )
                        } else {
                            dialog.dismiss()
                            file.mkdirs()
                            folderListener(file)
                        }
                    }
                }
            } else {
                val folder = folderList[adapterPosition]
                selected = folder
                if (folder.isDirectory) {
                    folderListener(folder)
                }
            }
        }
    }

    override fun getItemCount() = folderList.size
    override fun getItemId(position: Int) = position.toLong()
    override fun getItemViewType(position: Int) = position
}