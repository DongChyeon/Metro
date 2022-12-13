package com.dongchyeon.metro.data.model

import com.dongchyeon.metro.data.model.dto.RealtimeArrivalList

data class SubwayInfo(
    val updnLine: String,
    val subwayList: List<RealtimeArrivalList>,
    val seatList: List<SeatInfo>
)
