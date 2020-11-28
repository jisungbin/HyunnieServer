package com.sungbin.hyunnieserver.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.sungbin.androidutils.extensions.hide
import com.sungbin.hyunnieserver.R
import com.sungbin.hyunnieserver.databinding.LayoutFileBinding
import com.sungbin.hyunnieserver.model.File
import com.sungbin.hyunnieserver.module.GlideApp
import com.sungbin.hyunnieserver.tool.util.FileUtil


/**
 * Created by SungBin on 2020-08-23.
 */

class FileAdapter(
    private val items: List<File>
) : RecyclerView.Adapter<FileAdapter.ViewHolder>() {

    private var onClickListener: OnClickListener? = null

    interface OnClickListener {
        fun onClick(file: File)
    }

    fun setOnClickListener(action: (File) -> Unit) {
        onClickListener = object : OnClickListener {
            override fun onClick(file: File) {
                action(file)
            }
        }
    }

    inner class ViewHolder(
        private val itemBinding: LayoutFileBinding,
    ) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bindViewHolder(file: File, listener: OnClickListener?) {
            with(itemBinding) {
                item = file
                GlideApp
                    .with(ivType.context)
                    .load(FileUtil.getTypeIcon(file.fileType))
                    .into(ivType)
                tvName.apply {
                    isSelected = true
                    setOnClickListener {
                        listener?.onClick(file)
                    }
                }
                if (file.size.isEmpty()) tvDot.hide(true)
                invalidateAll()
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int) =
        ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(viewGroup.context),
                R.layout.layout_file, viewGroup, false
            )
        )

    override fun onBindViewHolder(@NonNull viewholder: ViewHolder, position: Int) {
        viewholder.bindViewHolder(items[position], onClickListener)
    }

    override fun getItemCount() = items.size
    override fun getItemId(position: Int) = position.toLong()
    override fun getItemViewType(position: Int) = position
}