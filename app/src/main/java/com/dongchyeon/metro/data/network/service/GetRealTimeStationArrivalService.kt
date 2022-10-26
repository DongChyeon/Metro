package com.dongchyeon.metro.data.network.service

import com.dongchyeon.metro.BuildConfig
import com.dongchyeon.metro.data.network.dto.RealTimeStationArrival
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface GetRealTimeStationArrivalService {
    @GET("/api/subway/${BuildConfig.API_KEY}/json/realtimeStationArrival/0/20/{statnNm}")
    suspend fun getRealTimeStationArrival(@Path("statnNm") statnNm: String): Response<RealTimeStationArrival>
}