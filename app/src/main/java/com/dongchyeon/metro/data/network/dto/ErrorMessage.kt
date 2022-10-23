package com.dongchyeon.metro.data.network.dto

import com.google.gson.annotations.SerializedName

data class ErrorMessage(
    @SerializedName("status")
    val status: Int,
    @SerializedName("code")
    val code: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("link")
    val link: String? = null,
    @SerializedName("developerMessage")
    val developerMessage: String,
    @SerializedName("total")
    val total: Int
)