package com.task.podcast.feature.home.data.fake


import com.task.podcast.feature.home.data.model.ContentModel
import com.task.podcast.feature.home.data.model.HomeSectionsModel
import com.task.podcast.feature.home.data.model.PaginationModel
import com.task.podcast.feature.home.data.model.SearchSectionsModel
import com.task.podcast.feature.home.data.model.SectionModel
import com.task.podcast.feature.home.domain.entity.ContentEntity
import com.task.podcast.feature.home.domain.entity.SectionEntity


object FakeData {
    fun getFakeHomeSectionsModel(list: List<SectionModel> = emptyList()) = HomeSectionsModel(
        sections = list, pagination = PaginationModel(
            nextPage = "/home_sections?page=2", totalPages = 10
        )
    )

    fun getFakeSearchSectionsModel(list: List<SectionModel> = emptyList()) =
        SearchSectionsModel(sections = list)

    fun getFakeSectionModel(contentModelList: List<ContentModel> = emptyList()) = SectionModel(
        name = "Trending Episodes",
        type = "2_lines_grid",
        contentType = "episode",
        order = "2",
        content = contentModelList
    )

    fun getFakeSectionEntity(contentEntityList: List<ContentEntity> = emptyList()) = SectionEntity(
        name = "Trending Episodes",
        type = "2_lines_grid",
        contentType = "episode",
        order = "2",
        content = contentEntityList
    )

    fun getFakeContentModel() = ContentModel(
        podcastId = "121493675",
        episodeId = "e35071e5-c224-58b8-9a33-93ab500c4832",
        audiobookId = null,
        articleId = null,
        name = "NPR News: 07-23-2024 4AM EDT",
        description = "NPR News: 07-23-2024 4AM EDT...",
        avatarUrl = "https://media.npr.org/assets/img/2023/03/01/npr-news-now_square.png",
        duration = 300,
        language = "en",
        score = "214.18567",
        episodeCount = null,
        priority = "5",
        popularityScore = "9",
        audioUrl = "https://chrt.fm/track/138C95/prfx.byspotify.com/e/play.podtrac.com/npr-500005/traffic.megaphone.fm/NPR6248253268.mp3",
        releaseDate = "2024-07-23T08:00:00.000Z",
        authorName = null
    )

    fun getFakeContentEntity() = ContentEntity(
        podcastId = "121493675",
        episodeId = "e35071e5-c224-58b8-9a33-93ab500c4832",
        audiobookId = null,
        articleId = null,
        name = "NPR News: 07-23-2024 4AM EDT",
        description = "NPR News: 07-23-2024 4AM EDT...",
        avatarUrl = "https://media.npr.org/assets/img/2023/03/01/npr-news-now_square.png",
        duration = 300,
        language = "en",
        score = "214.18567",
        episodeCount = "",
        priority = "5",
        popularityScore = "9",
        audioUrl = "https://chrt.fm/track/138C95/prfx.byspotify.com/e/play.podtrac.com/npr-500005/traffic.megaphone.fm/NPR6248253268.mp3",
        releaseDate = "2024-07-23T08:00:00.000Z",
        authorName = null
    )
}
