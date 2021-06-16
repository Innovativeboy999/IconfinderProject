package com.example.iconfinder.data.vo


import com.google.gson.annotations.SerializedName

data class License(
    @SerializedName("license_id")
    val licenseId: Int,
    val name: String,
    val scope: String
)