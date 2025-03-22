package com.task.podcast.feature.home.data.model

import com.google.gson.annotations.SerializedName

data class SectionModel (
    @SerializedName("name") val name: String?,
    @SerializedName("type") val type: String?,
    @SerializedName("content_type") val contentType: String?,
    @SerializedName("order") val order: String?,
    @SerializedName("content") val content: List<ContentModel>?
)