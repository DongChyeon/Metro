package com.dongchyeon.metro.di

import android.content.Context
import com.dongchyeon.metro.BuildConfig
import com.dongchyeon.metro.data.datasource.SubwayDataSource
import com.dongchyeon.metro.data.datasource.service.GetRealTimeStationArrivalService
import com.dongchyeon.metro.data.repository.SubwayRepository
import com.dongchyeon.metro.repository.BleRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
    fun providesOkHttpClient() = if (BuildConfig.DEBUG) {
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
    fun providesRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("http://swopenAPI.seoul.go.kr")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun providesSubwayService(retrofit: Retrofit): GetRealTimeStationArrivalService {
        return retrofit.create(GetRealTimeStationArrivalService::class.java)
    }

    @Provides
    @Singleton
    fun providesSubwayDataSource(subwayService: GetRealTimeStationArrivalService): SubwayDataSource {
        return SubwayDataSource(subwayService)
    }

    @Provides
    @Singleton
    fun providesSubwayRepository(subwayDataSource: SubwayDataSource): SubwayRepository {
        return SubwayRepository(subwayDataSource)
    }

    @Provides
    @Singleton
    fun providesBleRepository(@ApplicationContext context: Context): BleRepository {
        return BleRepository(context)
    }
}