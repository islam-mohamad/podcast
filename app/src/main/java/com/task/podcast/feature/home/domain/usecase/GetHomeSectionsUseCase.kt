package com.task.podcast.feature.home.domain.usecase

import com.task.podcast.core.domain.None
import com.task.podcast.core.domain.UseCase
import com.task.podcast.feature.home.domain.entity.SectionEntity
import com.task.podcast.feature.home.domain.repository.SectionsRepository
import javax.inject.Inject

class GetHomeSectionsUseCase @Inject constructor(private val repository: SectionsRepository) :
    UseCase<None, List<SectionEntity>>() {
    override suspend fun invoke(params: None): List<SectionEntity> {
        return repository.getHomeSections()
    }
}
