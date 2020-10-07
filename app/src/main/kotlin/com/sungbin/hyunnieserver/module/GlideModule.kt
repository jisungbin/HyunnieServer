package com.sungbin.hyunnieserver.module

import android.content.Context
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions
import com.sungbin.hyunnieserver.R

@com.bumptech.glide.annotation.GlideModule
class GlideModule : AppGlideModule() {
    override fun applyOptions(context: Context, builder: GlideBuilder) {
        builder.setDefaultRequestOptions(
            RequestOptions()
                .format(DecodeFormat.PREFER_ARGB_8888)
                .placeholder(R.drawable.ic_baseline_insert_drive_file_24)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
        )
    }
}