package com.dongchyeon.metro.data

import com.dongchyeon.metro.data.network.dto.RealtimeArrivalList

data class SubwayInfo(
    val updnLine: String,
    val subwayList: List<RealtimeArrivalList>,
    val seatList: List<SeatInfo>
)
