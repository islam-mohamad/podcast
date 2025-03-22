package com.task.podcast.feature.home.domain.usecase

import com.task.podcast.feature.home.data.fake.FakeData
import com.task.podcast.feature.home.domain.repository.SectionsRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.never
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class SearchUseCaseTest {

    @Mock
    private lateinit var repository: SectionsRepository

    private lateinit var searchUseCase: SearchUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        searchUseCase = SearchUseCase(repository)
    }

    @Test
    fun `invoke with blank query returns empty list without repository call`() = runTest {
        // When
        whenever(repository.search(Mockito.anyString())).thenReturn(emptyList())
        val result = searchUseCase("")

        // Then
        assertTrue(result.isEmpty())
        verify(repository, never()).search(any())
    }

    @Test
    fun `invoke with valid query returns repository results`() = runTest {
        // Given
        val testQuery = "test"
        val expectedResults = listOf(FakeData.getFakeSectionEntity())
        whenever(repository.search(testQuery)).thenReturn(expectedResults)

        // When
        val result = searchUseCase(testQuery)

        // Then
        assertEquals(expectedResults, result)
        verify(repository).search(testQuery)
    }

    @Test(expected = RuntimeException::class)
    fun `invoke throws exception when repository fails`() =
        runTest {
            // Given
            whenever(repository.search(Mockito.anyString())).thenThrow(RuntimeException("Network error"))
            // When
            searchUseCase("test")

            // Then: Expect exception
        }
}