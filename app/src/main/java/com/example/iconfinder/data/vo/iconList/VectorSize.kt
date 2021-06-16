package com.example.iconfinder.data.vo.iconList


import com.google.gson.annotations.SerializedName

data class VectorSize(
    val formats: List<FormatX>,
    val size: Int,
    @SerializedName("size_height")
    val sizeHeight: Int,
    @SerializedName("size_width")
    val sizeWidth: Int,
    @SerializedName("target_sizes")
    val targetSizes: List<List<Int>>
)