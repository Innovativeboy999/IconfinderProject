package com.example.iconfinder.data.vo.iconList


import com.google.gson.annotations.SerializedName

data class Price(
    val currency: String,
    val license: License,
    val price: Double
)