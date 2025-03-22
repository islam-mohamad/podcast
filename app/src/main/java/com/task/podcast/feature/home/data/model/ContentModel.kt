package com.task.podcast.feature.home.data.model
import com.google.gson.annotations.SerializedName

data class ContentModel (
    @SerializedName("podcast_id") val podcastId: String? = null,
    @SerializedName("episode_id") val episodeId: String? = null,
    @SerializedName("audiobook_id") val audiobookId: String? = null,
    @SerializedName("article_id") val articleId: String? = null,

    @SerializedName("name") val name: String?,
    @SerializedName("author_name") val authorName: String? = null,
    @SerializedName("description") val description: String?,
    @SerializedName("avatar_url") val avatarUrl: String?,
    @SerializedName("episode_count") val episodeCount: String? = null,
    @SerializedName("duration") val duration: Int?,
    @SerializedName("language") val language: String? = null,
    @SerializedName("priority") val priority: String? = null,
    @SerializedName("popularityScore") val popularityScore: String? = null,
    @SerializedName("score") val score: String? = null,

    @SerializedName("season_number") val seasonNumber: Int? = null,
    @SerializedName("episode_type") val episodeType: String? = null,
    @SerializedName("podcast_name") val podcastName: String? = null,
    @SerializedName("number") val number: Int? = null,
    @SerializedName("separated_audio_url") val separatedAudioUrl: String? = null,
    @SerializedName("audio_url") val audioUrl: String? = null,
    @SerializedName("release_date") val releaseDate: String? = null,

    @SerializedName("podcastPopularityScore") val podcastPopularityScore: Int? = null,
    @SerializedName("podcastPriority") val podcastPriority: Int? = null,

    @SerializedName("paid_is_early_access") val paidIsEarlyAccess: Boolean? = null,
    @SerializedName("paid_is_now_early_access") val paidIsNowEarlyAccess: Boolean? = null,
    @SerializedName("paid_is_exclusive") val paidIsExclusive: Boolean? = null,
    @SerializedName("paid_transcript_url") val paidTranscriptUrl: String? = null,
    @SerializedName("free_transcript_url") val freeTranscriptUrl: String? = null,
    @SerializedName("paid_is_exclusive_partially") val paidIsExclusivePartially: Boolean? = null,
    @SerializedName("paid_exclusive_start_time") val paidExclusiveStartTime: Int? = null,
    @SerializedName("paid_early_access_date") val paidEarlyAccessDate: String? = null,
    @SerializedName("paid_early_access_audio_url") val paidEarlyAccessAudioUrl: String? = null,
    @SerializedName("paid_exclusivity_type") val paidExclusivityType: String? = null
)