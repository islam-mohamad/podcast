package com.task.podcast.feature.home.data.model.mapper

import com.task.podcast.feature.home.data.model.ContentModel
import com.task.podcast.feature.home.data.model.SectionModel
import com.task.podcast.feature.home.domain.entity.ContentEntity
import com.task.podcast.feature.home.domain.entity.SectionEntity

fun SectionModel.toEntity() = SectionEntity(
    name = name.orEmpty(),
    type = type.orEmpty(),
    contentType = contentType.orEmpty(),
    order = order.orEmpty(),
    content = content?.map { it.toEntity() } ?: emptyList()
)

fun ContentModel.toEntity() = ContentEntity(
    podcastId = podcastId,
    episodeId = episodeId,
    audiobookId = audiobookId,
    articleId = articleId,
    name = name ?: "",
    authorName = authorName,
    description = description ?: "",
    avatarUrl = avatarUrl ?: "",
    episodeCount = episodeCount.orEmpty(),
    duration = duration ?: 0,
    language = language,
    priority = priority.orEmpty(),
    popularityScore = popularityScore.orEmpty(),
    score = score.orEmpty(),
    seasonNumber = seasonNumber,
    episodeType = episodeType,
    podcastName = podcastName,
    number = number,
    separatedAudioUrl = separatedAudioUrl,
    audioUrl = audioUrl,
    releaseDate = releaseDate,
    paidIsEarlyAccess = paidIsEarlyAccess,
    paidIsNowEarlyAccess = paidIsNowEarlyAccess,
    paidIsExclusive = paidIsExclusive,
    paidTranscriptUrl = paidTranscriptUrl,
    freeTranscriptUrl = freeTranscriptUrl,
    paidIsExclusivePartially = paidIsExclusivePartially,
    paidExclusiveStartTime = paidExclusiveStartTime,
    paidEarlyAccessDate = paidEarlyAccessDate,
    paidEarlyAccessAudioUrl = paidEarlyAccessAudioUrl,
    paidExclusivityType = paidExclusivityType
)