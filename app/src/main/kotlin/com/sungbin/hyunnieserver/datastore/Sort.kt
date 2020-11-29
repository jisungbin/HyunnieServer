package com.sungbin.hyunnieserver.datastore

sealed class Sort {
    object FOLDER : Sort()
    object FILE : Sort()
    object GANADA : Sort()
    object DANAGA : Sort()
}