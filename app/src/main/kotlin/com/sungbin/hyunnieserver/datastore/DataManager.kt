package com.sungbin.hyunnieserver.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import kotlinx.coroutines.flow.map


/**
 * Created by SungBin on 2020-11-29.
 */

object DataManager {

    private lateinit var context: Context

    private val sortNameDataStore by lazy { dataStoreOf("sort_name_pref") }
    private val sortTypeDataStore by lazy { dataStoreOf("sort_type_pref") }
    private val uiThemeDataStore by lazy { dataStoreOf("ui_theme_pref") }

    private val sortNameKey by lazy { prefKeyOf<Int>("sort_name_key") }
    private val sortTypeKey by lazy { prefKeyOf<Int>("sort_type_key") }
    private val uiThemeKey by lazy { prefKeyOf<Int>("ui_theme_key") }

    private fun dataStoreOf(name: String) = context.createDataStore(name = name)
    private inline fun <reified T : Any> prefKeyOf(key: String) = preferencesKey<T>(key)

    suspend fun setUiTheme(uiTheme: Int) {
        uiThemeDataStore.edit { preference ->
            preference[uiThemeKey] = uiTheme
        }
    }

    suspend fun setSortName(sortName: Int) {
        sortNameDataStore.edit { preference ->
            preference[sortNameKey] = sortName
        }
    }

    suspend fun setSortType(sortType: Int) {
        sortTypeDataStore.edit { preference ->
            preference[sortTypeKey] = sortType
        }
    }

    val uiThemeFlow by lazy {
        uiThemeDataStore.data.map { preference ->
            preference[uiThemeKey] ?: UiTheme.DAY
        }
    }

    val sortNameFlow by lazy {
        sortNameDataStore.data.map { preference ->
            preference[sortNameKey] ?: Sort.GANADA
        }
    }

    val sortTypeFlow by lazy {
        sortTypeDataStore.data.map { preference ->
            preference[sortTypeKey] ?: Sort.FOLDER
        }
    }

    fun init(context: Context) {
        this.context = context
    }
}