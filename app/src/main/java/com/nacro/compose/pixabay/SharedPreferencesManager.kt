package com.nacro.compose.pixabay

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nacro.compose.pixabay.domain.DefaultLayout
import java.lang.Exception

class SharedPreferencesManager(context: Context) {
    companion object {
        private const val SHARED_PREFERENCE_COMMON = "KEY_SHARED_PREFERENCE_COMMON"

        private const val KEY_QUERY_HISTORY = "KEY_QUERY_HISTORY"
        private const val KEY_DEFAULT_LAYOUT = "KEY_DEFAULT_LAYOUT"
    }



    private val prefsCommon = context.getSharedPreferences(SHARED_PREFERENCE_COMMON, MODE_PRIVATE)

    /**
     * 查詢歷史
     */
    var queryHistory: Set<String>
        get() {
            return prefsCommon.getStringSet(KEY_QUERY_HISTORY, setOf()) ?: setOf()
//            return try {
//                val type = TypeToken.getArray(String::class.java).type
//                Gson().fromJson<Array<String>>(str, type).asList()
//            } catch (e: Exception) {
//                listOf()
//            }
        }
        set(value) {
            try {
                prefsCommon.edit().putStringSet(KEY_QUERY_HISTORY, value).apply()
            } catch (_: Exception) {

            }
        }

    var defaultLayout: DefaultLayout
        get() {
            return DefaultLayout.get(prefsCommon.getString(KEY_DEFAULT_LAYOUT, "") ?: "")
        }
        set(value) {
            prefsCommon.edit().putString(KEY_DEFAULT_LAYOUT, value.name).apply()
        }
}