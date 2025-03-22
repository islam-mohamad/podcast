package com.task.podcast.feature.home.data.repository

import com.task.podcast.core.data.repository.BaseRepository
import com.task.podcast.core.data.repository.BaseRepositoryImpl
import com.task.podcast.feature.home.data.fake.FakeData
import com.task.podcast.feature.home.data.source.remote.HomeApiService
import com.task.podcast.feature.home.data.source.remote.SearchApiService
import com.task.podcast.feature.home.domain.repository.SectionsRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever

class SectionsRepositoryImplTest {

    private lateinit var homeApiService: HomeApiService
    private lateinit var searchApiService: SearchApiService
    private lateinit var baseRepository: BaseRepository
    private lateinit var repository: SectionsRepository

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        homeApiService = Mockito.mock(HomeApiService::class.java)
        searchApiService = Mockito.mock(SearchApiService::class.java)
        baseRepository = BaseRepositoryImpl(UnconfinedTestDispatcher())
        repository = SectionsRepositoryImpl(homeApiService, searchApiService, baseRepository)
    }

    @Test
    fun `getHomeSections returns mapped sections`() = runTest {
        // Given
        val fakeSectionModel = FakeData.getFakeSectionModel(listOf(FakeData.getFakeContentModel()))
        val fakeHomeResponse = FakeData.getFakeHomeSectionsModel(listOf(fakeSectionModel))
        whenever(homeApiService.getHomeSections()).thenReturn(fakeHomeResponse)

        // When
        val result = repository.getHomeSections()

        // Then
        assertEquals(1, result.size)
        assertEquals(
            FakeData.getFakeSectionEntity(listOf(FakeData.getFakeContentEntity())),
            result.first()
        )
    }

    @Test
    fun `search returns mapped sections`() = runTest {
        // Given
        val query = "test"
        val fakeSectionModel = FakeData.getFakeSectionModel(listOf(FakeData.getFakeContentModel()))
        val fakeSearchResponse = FakeData.getFakeSearchSectionsModel(listOf(fakeSectionModel))
        whenever(searchApiService.search(query)).thenReturn(fakeSearchResponse)

        // When
        val result = repository.search(query)

        // Then
        assertEquals(1, result.size)
        assertEquals(
            FakeData.getFakeSectionEntity(listOf(FakeData.getFakeContentEntity())),
            result.first()
        )
    }

    @Test(expected = RuntimeException::class)
    fun `getHomeSections throws mapped network exception`() = runTest {
        // Given
        whenever(homeApiService.getHomeSections()).thenThrow(RuntimeException("Network error"))

        // When
        repository.getHomeSections()

        // Then: Expect exception
    }
}
