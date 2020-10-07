package com.sungbin.hyunnieserver.model

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.sungbin.hyunnieserver.GlideApp
import com.sungbin.hyunnieserver.tool.util.FileUtil


/**
 * Created by SungBin on 2020-08-31.
 */

data class File(
    val name: String,
    val size: String,
    val originSize: Long,
    val path: String,
    val fileType: FileType,
    val lastModify: String
) {
    companion object {
        @JvmStatic
        @BindingAdapter("typeSrc")
        fun setTypeSrc(imageView: ImageView, fileType: FileType) {
            GlideApp
                .with(imageView.context)
                .load(FileUtil.getTypeIcon(fileType))
                .into(imageView)
        }
    }
}