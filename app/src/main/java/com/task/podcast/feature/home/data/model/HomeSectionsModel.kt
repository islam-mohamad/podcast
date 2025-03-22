package com.task.podcast.feature.home.data.model

import com.google.gson.annotations.SerializedName

data class HomeSectionsModel(
    @SerializedName("sections") val sections: List<SectionModel>?,
    @SerializedName("pagination") val pagination: PaginationModel?
)