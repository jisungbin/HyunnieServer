package com.sungbin.hyunnieserver.tool.util

import android.app.Activity

interface OnBackPressedUtil {
    fun onBackPressed(activity: Activity): Boolean
}