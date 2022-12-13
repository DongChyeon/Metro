package com.dongchyeon.metro.data.model

data class SeatInfo(
    val trainLineNm: String,
    val pregnant: List<Int>,
    val elderly: List<Int>
)