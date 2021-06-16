package com.example.iconfinder.data.vo.iconList


import com.google.gson.annotations.SerializedName

data class FormatX(
    @SerializedName("download_url")
    val downloadUrl: String,
    val format: String
)