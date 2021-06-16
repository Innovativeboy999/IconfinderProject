package com.example.iconfinder.data.vo.iconList


import com.google.gson.annotations.SerializedName

data class Container(
    @SerializedName("download_url")
    val downloadUrl: String,
    val format: String
)