package com.task.podcast.feature.home.data.source.remote

import com.task.podcast.feature.home.data.model.SearchSectionsModel
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchApiService {
    @GET("https://mock.apidog.com/m1/735111-711675-default/search")
    suspend fun search(@Query("query") query: String): SearchSectionsModel
}
