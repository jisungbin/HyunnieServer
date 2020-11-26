package com.sungbin.hyunnieserver.model


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
)