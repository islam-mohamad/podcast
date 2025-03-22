package com.task.podcast.feature.home.data.model

import com.google.gson.annotations.SerializedName

data class SearchSectionsModel(
    @SerializedName("sections")
    val sections: List<SectionModel>?
)