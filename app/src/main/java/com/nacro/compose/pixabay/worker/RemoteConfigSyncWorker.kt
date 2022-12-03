package com.nacro.compose.pixabay.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.nacro.compose.pixabay.SharedPreferencesManager
import com.nacro.compose.pixabay.domain.DefaultLayout
import org.koin.java.KoinJavaComponent.inject

class RemoteConfigSyncWorker(context: Context, params: WorkerParameters): Worker(context, params) {
    companion object {
        private const val KEY_LAYOUT = "layout"
    }

    private val preference: SharedPreferencesManager by inject(SharedPreferencesManager::class.java)

    override fun doWork(): Result {
        val remoteConfig = Firebase.remoteConfig

        remoteConfig.fetchAndActivate()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val layoutName = remoteConfig.getString(KEY_LAYOUT)
                    preference.defaultLayout = DefaultLayout.get(layoutName)
                }
            }
        return Result.success()
    }

}

sealed class Layout(val name: String)

object GridLayout: Layout("grid")
object ListLayout: Layout("list")