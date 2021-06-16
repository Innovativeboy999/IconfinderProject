package com.example.iconfinder.data.vo


import com.google.gson.annotations.SerializedName

data class RasterSize(
    val formats: List<Format>,
    val size: Int,
    @SerializedName("size_height")
    val sizeHeight: Int,
    @SerializedName("size_width")
    val sizeWidth: Int
)