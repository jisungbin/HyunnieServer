package com.sungbin.hyunnieserver.model

sealed class FileType {

    object FOLDER : FileType()
    object IMAGE : FileType()
    object TEXT : FileType()
    object VIDEO : FileType()
    object GIF : FileType()
    object CODE : FileType()
    object APK : FileType()
    object ZIP : FileType()
    object EXE : FileType()
    object MUSIC : FileType()
    object BOOK : FileType()
    object PDF : FileType()
    object SUBTITLE : FileType()
    object FILE : FileType()

}