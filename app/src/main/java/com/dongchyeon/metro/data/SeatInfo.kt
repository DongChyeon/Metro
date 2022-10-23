package com.dongchyeon.metro.data

data class SeatInfo(
    val trainLineNm: String,
    val pregnant: List<Int>,
    val elderly: List<Int>
)