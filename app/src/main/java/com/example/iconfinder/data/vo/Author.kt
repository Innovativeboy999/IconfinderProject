package com.example.iconfinder.data.vo


import com.google.gson.annotations.SerializedName

data class Author(
    val company: String,
    @SerializedName("iconsets_count")
    val iconsetsCount: Int,
    @SerializedName("is_designer")
    val isDesigner: Boolean,
    val name: String,
    @SerializedName("user_id")
    val userId: Int,
    val username: String
)