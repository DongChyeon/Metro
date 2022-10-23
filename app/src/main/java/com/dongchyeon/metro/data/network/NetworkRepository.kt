package com.dongchyeon.metro.data.network

import com.dongchyeon.metro.data.network.service.GetRealTimeStationArrivalService
import javax.inject.Inject

class NetworkRepository @Inject constructor(private val subwayService: GetRealTimeStationArrivalService) {

    suspend fun getRealTimeStationArrival(statnNm: String) =
        subwayService.getRealTimeStationArrival(statnNm)
}