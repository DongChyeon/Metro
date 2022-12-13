package com.dongchyeon.metro.data.model.dto

import com.google.gson.annotations.SerializedName

data class RealTimeStationArrival(
    @SerializedName("errorMessage")
    val errorMessage: ErrorMessage,
    @SerializedName("realtimeArrivalList")
    val realtimeArrivalList: List<RealtimeArrivalList>
)
