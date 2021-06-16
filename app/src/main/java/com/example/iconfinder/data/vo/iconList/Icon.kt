package com.example.iconfinder.data.vo.iconList


import com.google.gson.annotations.SerializedName

data class Icon(
    val categories: List<Category>,
    val containers: List<Container>,
    @SerializedName("icon_id")
    val iconId: Int,
    @SerializedName("is_icon_glyph")
    val isIconGlyph: Boolean,
    @SerializedName("is_premium")
    val isPremium: Boolean,
    @SerializedName("is_purchased")
    val isPurchased: Boolean,
    val prices: List<Price>,
    @SerializedName("published_at")
    val publishedAt: String,
    @SerializedName("raster_sizes")
    val rasterSizes: List<RasterSize>,
    val styles: List<Style>,
    val tags: List<String>,
    val type: String,
    @SerializedName("vector_sizes")
    val vectorSizes: List<VectorSize>
)