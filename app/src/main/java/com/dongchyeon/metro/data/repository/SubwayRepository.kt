package com.dongchyeon.metro.data.repository

import com.dongchyeon.metro.data.datasource.SubwayDataSource
import com.dongchyeon.metro.data.model.dto.RealTimeStationArrival
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SubwayRepository @Inject constructor(
    private val subwayDataSource: SubwayDataSource
) {
    suspend fun getRealTimeStationArrival(statnNm: String): Flow<RealTimeStationArrival> =
        subwayDataSource.getRealTimeStationArrival(statnNm)
}