package com.task.podcast.feature.home.domain.usecase

import com.task.podcast.core.domain.UseCase
import com.task.podcast.feature.home.domain.entity.SectionEntity
import com.task.podcast.feature.home.domain.repository.SectionsRepository
import javax.inject.Inject

class SearchUseCase @Inject constructor(
    private val repository: SectionsRepository
) : UseCase<String, List<SectionEntity>>() {

    override suspend fun invoke(params: String): List<SectionEntity> {
        if (params.isBlank()) {
            return emptyList()
        }
        return repository.search(params)
    }
}