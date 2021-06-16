package com.example.iconfinder.data.vo.iconList


import com.google.gson.annotations.SerializedName

data class Format(
    @SerializedName("download_url")
    val downloadUrl: String,
    val format: String,
    @SerializedName("preview_url")
    val previewUrl: String
)