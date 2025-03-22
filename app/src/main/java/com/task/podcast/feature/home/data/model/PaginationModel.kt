package com.task.podcast.feature.home.data.model

import com.google.gson.annotations.SerializedName

data class PaginationModel(
    @SerializedName("next_page") val nextPage: String?,
    @SerializedName("total_pages") val totalPages: Int?
)