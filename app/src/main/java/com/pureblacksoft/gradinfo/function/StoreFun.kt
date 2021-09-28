package com.pureblacksoft.gradinfo.function

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "prefs")

class StoreFun(private val context: Context) {
    companion object {
        val KEY_ID_THEME = intPreferencesKey("theme_id")
    }

    val themeIdFlow: Flow<Int> = context.dataStore.data.map {
        it[KEY_ID_THEME] ?: 0
    }

    suspend fun storePref(themeId: Int) {
        context.dataStore.edit {
            it[KEY_ID_THEME] = themeId
        }
    }
}

//PureBlack Software / Murat BIYIK