package com.sungbin.hyunnieserver.tool.util


/**
 * Created by SungBin on 2020-11-27.
 */

sealed class ArrayPosition {
    object FIRST : ArrayPosition()
    object LAST : ArrayPosition()
}

fun <T> List<T>.removePosition(position: ArrayPosition): List<T> {
    return when (position) {
        ArrayPosition.FIRST -> this.toCollection(ArrayList()).apply {
            removeAt(0)
        }.toList()
        ArrayPosition.LAST -> this.toCollection(ArrayList()).apply {
            removeAt(this.lastIndex)
        }.toList()
    }
}