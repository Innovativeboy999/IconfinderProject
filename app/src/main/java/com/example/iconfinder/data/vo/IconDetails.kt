package com.example.iconfinder.data.vo


import com.google.gson.annotations.SerializedName

data class IconDetails(
    val categories: List<Category>,
    val containers: List<Any>,
    @SerializedName("icon_id")
    val iconId: Int,
    val iconset: Iconset,
    @SerializedName("is_icon_glyph")
    val isIconGlyph: Boolean,
    @SerializedName("is_premium")
    val isPremium: Boolean,
    @SerializedName("published_at")
    val publishedAt: String,
    @SerializedName("raster_sizes")
    val rasterSizes: List<RasterSize>,
    val styles: List<StyleX>,
    val tags: List<String>,
    val type: String,
    @SerializedName("vector_sizes")
    val vectorSizes: List<VectorSize>
)