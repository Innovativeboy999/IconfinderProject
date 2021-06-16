package com.example.iconfinder.data.vo.iconList


import com.google.gson.annotations.SerializedName

data class License(
    @SerializedName("license_id")
    val licenseId: Int,
    val name: String,
    val scope: String,
    val url: String
)