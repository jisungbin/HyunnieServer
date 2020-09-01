package com.sungbin.hyunnieserver.tool.util

import android.content.Context
import com.sungbin.hyunnieserver.ui.activity.ExceptionActivity
import com.sungbin.sungbintool.LogUtils
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.newTask

object ExceptionUtil {

    fun except(exception: Exception, context: Context) {
        LogUtils.w(exception)
        val message = exception.localizedMessage
        val line = exception.stackTrace[0].lineNumber
        val content = "$message #$line"
        context.startActivity(context.intentFor<ExceptionActivity>("message" to content).newTask())
    }

}