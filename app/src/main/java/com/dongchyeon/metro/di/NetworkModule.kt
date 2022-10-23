package com.dongchyeon.metro.di

import com.dongchyeon.metro.BuildConfig
import com.dongchyeon.metro.data.network.NetworkRepository
import com.dongchyeon.metro.data.network.service.GetRealTimeStationArrivalService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Singleton
    @Provides
    fun provideOkHttpClient() = if (BuildConfig.DEBUG) {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    } else {
        OkHttpClient.Builder().build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("http://swopenAPI.seoul.go.kr")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideSubwayServcie(retrofit: Retrofit): GetRealTimeStationArrivalService {
        return retrofit.create(GetRealTimeStationArrivalService::class.java)
    }

    @Provides
    @Singleton
    fun provideNetworkRepository(subwayService: GetRealTimeStationArrivalService) =
        NetworkRepository(subwayService)
}