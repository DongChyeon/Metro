package com.dongchyeon.metro.data.model.dto

import com.google.gson.annotations.SerializedName

data class RealtimeArrivalList(
    @SerializedName("beginRow")
    val beginRow: String,
    @SerializedName("endRow")
    val endRow: String,
    @SerializedName("curPage")
    val curPage: String,
    @SerializedName("pageRow")
    val pageRow: String,
    @SerializedName("totalCount")
    val totalCount: Int,
    @SerializedName("rowNum")
    val rowNum: Int,
    @SerializedName("selectedCount")
    val selectedCount: Int,
    @SerializedName("subwayId")
    val subwayId: String,
    @SerializedName("subwayNm")
    val subwayNm: String,
    @SerializedName("updnLine")
    val updnLine: String,
    @SerializedName("trainLineNm")
    val trainLineNm: String,
    @SerializedName("subwayHeading")
    val subwayHeading: String,
    @SerializedName("statnFid")
    val statnFid: String,
    @SerializedName("statnTid")
    val statnTid: String,
    @SerializedName("statnId")
    val statnId: String,
    @SerializedName("statnNm")
    val statnNm: String,
    @SerializedName("trainCo")
    val trainCo: String,
    @SerializedName("ordkey")
    val ordkey: String,
    @SerializedName("subwayList")
    val subwayList: String,
    @SerializedName("statnList")
    val statnList: String,
    @SerializedName("btrainSttus")
    val btrainSttus: String,
    @SerializedName("barvlDt")
    val barvlDt: String,
    @SerializedName("btrainNo")
    val btrainNo: String,
    @SerializedName("bstatnId")
    val bstatnId: String,
    @SerializedName("bstatnNm")
    val bstatnNm: String,
    @SerializedName("recptnDt")
    val recptnDt: String,
    @SerializedName("arvlMsg2")
    val arvlMsg2: String,
    @SerializedName("arvlMsg3")
    val arvlMsg3: String,
    @SerializedName("arvlCd")
    val arvlCd: String
)
