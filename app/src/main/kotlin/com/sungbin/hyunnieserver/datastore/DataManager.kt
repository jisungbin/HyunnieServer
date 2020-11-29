package com.sungbin.hyunnieserver.datastore

import android.content.Context
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.preferencesKey
import kotlinx.coroutines.flow.map


/**
 * Created by SungBin on 2020-11-29.
 */

object DataManager {

    private lateinit var context: Context

    private val sortNameDataStore = dataStoreOf("sort_name_pref")
    private val sortTypeDataStore = dataStoreOf("sort_type_pref")
    private val uiThemeDataStore = dataStoreOf("ui_theme_pref")

    private val sortNameKey = prefKeyOf<Int>("sort_name_key")
    private val sortTypeKey = prefKeyOf<Int>("sort_type_key")
    private val uiThemeKey = prefKeyOf<Int>("ui_theme_key")

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

    val uiThemeFlow = uiThemeDataStore.data.map { preference ->
        preference[uiThemeKey] ?: UiTheme.DAY
    }

    val sortNameFlow = sortNameDataStore.data.map { preference ->
        preference[sortNameKey] ?: Sort.GANADA
    }

    val sortTypeFlow = sortTypeDataStore.data.map { preference ->
        preference[sortTypeKey] ?: Sort.FOLDER
    }

    fun init(context: Context) {
        this.context = context
    }
}