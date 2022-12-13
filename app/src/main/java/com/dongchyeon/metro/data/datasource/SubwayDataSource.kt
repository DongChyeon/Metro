package com.dongchyeon.metro.data.datasource

import com.dongchyeon.metro.data.datasource.service.GetRealTimeStationArrivalService
import com.dongchyeon.metro.data.model.dto.RealTimeStationArrival
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class SubwayDataSource @Inject constructor(
    private val subwayService: GetRealTimeStationArrivalService
) {
    suspend fun getRealTimeStationArrival(statnNm: String): Flow<RealTimeStationArrival> = flow {
        val response = subwayService.getRealTimeStationArrival(statnNm)
        if (response.isSuccessful) {
            response.body()?.let {
                emit(it)
            }
        }
    }.flowOn(Dispatchers.IO)
}