package com.task.podcast.feature.home.data.source.remote

import com.task.podcast.feature.home.data.model.HomeSectionsModel
import retrofit2.http.GET

interface HomeApiService {
    @GET("home_sections")
    suspend fun getHomeSections(): HomeSectionsModel
}
