package com.example.iconfinder.data.vo


import com.google.gson.annotations.SerializedName

data class Iconset(
    @SerializedName("are_all_icons_glyph")
    val areAllIconsGlyph: Boolean,
    val author: Author,
    val categories: List<CategoryX>,
    @SerializedName("icons_count")
    val iconsCount: Int,
    @SerializedName("iconset_id")
    val iconsetId: Int,
    val identifier: String,
    @SerializedName("is_premium")
    val isPremium: Boolean,
    val license: License,
    val name: String,
    @SerializedName("published_at")
    val publishedAt: String,
    val styles: List<Style>,
    val type: String,
    @SerializedName("website_url")
    val websiteUrl: String
)