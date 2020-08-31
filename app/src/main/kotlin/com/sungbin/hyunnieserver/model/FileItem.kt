package com.sungbin.hyunnieserver.model

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.sungbin.hyunnieserver.GlideApp
import com.sungbin.hyunnieserver.tool.util.FileUtil


/**
 * Created by SungBin on 2020-08-31.
 */

data class FileItem(
    val name: String,
    val size: String,
    val path: String,
    val type: Int,
    val lastModify: String
) {
    companion object {
        @JvmStatic
        @BindingAdapter("typeSrc")
        fun setTypeSrc(imageView: ImageView, type: Int) {
            GlideApp
                .with(imageView.context)
                .load(FileUtil.getTypeIcon(type))
                .into(imageView)
        }
    }
}