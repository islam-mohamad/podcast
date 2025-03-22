package com.task.podcast.feature.home.domain.usecase

import com.task.podcast.core.domain.None
import com.task.podcast.feature.home.data.fake.FakeData
import com.task.podcast.feature.home.domain.entity.SectionEntity
import com.task.podcast.feature.home.domain.repository.SectionsRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class GetHomeSectionsUseCaseTest {

    @Mock
    private lateinit var repository: SectionsRepository

    private lateinit var getHomeSectionsUseCase: GetHomeSectionsUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        getHomeSectionsUseCase = GetHomeSectionsUseCase(repository)
    }

    @Test
    fun `invoke returns list of home sections`() = runTest {
        // Given
        val expectedSections: List<SectionEntity> = listOf(FakeData.getFakeSectionEntity())
        whenever(repository.getHomeSections()).thenReturn(expectedSections)

        // When
        val result = getHomeSectionsUseCase.invoke(None)

        // Then
        assertEquals(expectedSections, result)
        verify(repository).getHomeSections()
    }

    @Test(expected = RuntimeException::class)
    fun `invoke throws exception when repository fails`() = runTest {
        // Given
        whenever(repository.getHomeSections()).thenThrow(RuntimeException("Error fetching home sections"))

        // When
        getHomeSectionsUseCase.invoke(None)

        // Then: Exception is expected
    }
}