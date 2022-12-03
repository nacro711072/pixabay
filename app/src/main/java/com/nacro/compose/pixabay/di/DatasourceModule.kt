package com.nacro.compose.pixabay.di

import com.nacro.compose.pixabay.BuildConfig
import com.nacro.compose.pixabay.Const
import com.nacro.compose.pixabay.SharedPreferencesManager
import com.nacro.compose.pixabay.api.PixabayApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val remoteDataModule = module {
    single {
        createOkHttpClient()
    }

    single {
        Retrofit.Builder()
            .baseUrl(Const.PIXABAY.API_DOMAIN)
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PixabayApiService::class.java)
    }
}

private fun createOkHttpClient(): OkHttpClient {
    return OkHttpClient.Builder()
        .apply {
            addInterceptor { chain ->
                val request = chain.request()
                val newUrl = request.url
                    .newBuilder()
                    .addQueryParameter("key", Const.PIXABAY.KEY)
                    .build()

                val newRequest = request
                    .newBuilder()
                    .url(newUrl)
                    .build()
                chain.proceed(newRequest)
            }

            if (BuildConfig.DEBUG) {
                val interceptor = HttpLoggingInterceptor()
                interceptor.level = HttpLoggingInterceptor.Level.BODY
                addInterceptor(interceptor)
            }
        }
        .build()
}

val localDataModule = module {
    single {
        SharedPreferencesManager(androidContext())
    }
}