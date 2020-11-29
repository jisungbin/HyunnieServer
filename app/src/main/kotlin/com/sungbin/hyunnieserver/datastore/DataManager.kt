package com.sungbin.hyunnieserver.datastore

import android.content.Context
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.preferencesKey


/**
 * Created by SungBin on 2020-11-29.
 */

class DataManager(private val context: Context) {


    private fun dataStore(name: String) = context.createDataStore(name = name)
    private fun <T> getKey(key: String, type: T) = preferencesKey<T>(key)

}