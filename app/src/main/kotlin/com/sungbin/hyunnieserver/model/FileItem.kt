package com.sungbin.hyunnieserver.model

import android.widget.ImageView
import androidx.databinding.BindingAdapter


/**
 * Created by SungBin on 2020-08-31.
 */

data class FileItem(val title: String, val size: Int, val path: String, val type: Int) {
    companion object {
        @JvmStatic
        @BindingAdapter("typeSrc")
        fun loadImage(imageView: ImageView, type: Int) {
            /*GlideApp
                .with(imageView.context)
                .load(url ?: R.drawable.ic_baseline_insert_drive_file_24)
                .into(imageView)*/
        }
    }
}