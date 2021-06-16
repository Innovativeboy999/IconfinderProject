package com.example.iconfinder.data.vo.iconList


import com.google.gson.annotations.SerializedName

data class ListIcons(
    val icons: List<Icon>,
    @SerializedName("total_count")
    val totalCount: Int
)