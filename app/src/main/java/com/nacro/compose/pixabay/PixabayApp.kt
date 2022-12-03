package com.nacro.compose.pixabay

import android.app.Application
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.nacro.compose.pixabay.di.*
import com.nacro.compose.pixabay.worker.RemoteConfigSyncWorker
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import java.util.concurrent.TimeUnit

class PixabayApp: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@PixabayApp)
            val moduleList = arrayListOf(
                remoteDataModule,
                localDataModule,
                mapperModule,
                repositoryModule,
                viewModelModule
            )

            modules(moduleList)
        }

        val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 60 // second
        }
        remoteConfig.setConfigSettingsAsync(configSettings)

        val worker = PeriodicWorkRequest.Builder(RemoteConfigSyncWorker::class.java, PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS, TimeUnit.MILLISECONDS)
            .build()
        WorkManager.getInstance(this).enqueue(worker)
    }
}