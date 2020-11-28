package com.sungbin.hyunnieserver.tool.util

import com.sungbin.androidutils.util.Logger


/**
 * Created by SungBin on 2020-11-27.
 */

sealed class ArrayPosition {
    object FIRST : ArrayPosition()
    object LAST : ArrayPosition()
}

fun <T> List<T>.removePosition(position: ArrayPosition): List<T> { // 이제 제너릭 사용 익숙해졌다 히히
    return try {
        when (position) {
            ArrayPosition.FIRST -> this.toCollection(ArrayList()).apply {
                removeAt(0)
            }
            ArrayPosition.LAST -> this.toCollection(ArrayList()).apply {
                removeAt(this.lastIndex)
            }
        }
    } catch (exception: Exception) {
        Logger.w("Error at `Array.removePosition`.", exception)
        this
    }
}