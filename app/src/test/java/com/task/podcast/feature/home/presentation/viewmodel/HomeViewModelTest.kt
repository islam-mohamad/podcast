package com.task.podcast.feature.home.presentation.viewmodel

import com.task.podcast.R
import com.task.podcast.core.domain.None
import com.task.podcast.feature.home.data.fake.FakeData
import com.task.podcast.feature.home.domain.usecase.GetHomeSectionsUseCase
import com.task.podcast.feature.home.presentation.model.HomeUiState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private lateinit var getHomeSectionsUseCase: GetHomeSectionsUseCase
    private lateinit var homeViewModel: HomeViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        getHomeSectionsUseCase = mock(GetHomeSectionsUseCase::class.java)
    }

    @Test
    fun `loadHomeSections emits Success state when use case returns non-empty data`() = runTest {
        val sampleSection = FakeData.getFakeSectionEntity(listOf(FakeData.getFakeContentEntity()))
        whenever(getHomeSectionsUseCase(None)).thenReturn(listOf(sampleSection))

        homeViewModel = HomeViewModel(getHomeSectionsUseCase, UnconfinedTestDispatcher())
        advanceUntilIdle()

        val uiState = homeViewModel.uiState.value
        assertTrue(uiState is HomeUiState.Success)
        uiState as HomeUiState.Success
        assertEquals(1, uiState.homeSections.size)
        assertEquals("Trending Episodes", uiState.homeSections[0].sectionName)
    }

    @Test
    fun `loadHomeSections emits Success state with empty list when use case returns empty list`() =
        runTest {
            whenever(getHomeSectionsUseCase(None)).thenReturn(emptyList())

            homeViewModel = HomeViewModel(getHomeSectionsUseCase, UnconfinedTestDispatcher())
            advanceUntilIdle()

            val uiState = homeViewModel.uiState.value
            assertTrue(uiState is HomeUiState.Success)
            uiState as HomeUiState.Success
            assertTrue(uiState.homeSections.isEmpty())
        }

    @Test
    fun `loadHomeSections emits Error state when use case throws exception`() = runTest {
        whenever(getHomeSectionsUseCase(None)).thenThrow(RuntimeException("Test error"))

        homeViewModel = HomeViewModel(getHomeSectionsUseCase, UnconfinedTestDispatcher())
        advanceUntilIdle()
        val uiState = homeViewModel.uiState.value
        assertTrue(uiState is HomeUiState.Error)
        uiState as HomeUiState.Error
        assertEquals(R.string.default_error_msg, uiState.messageResId)
    }
}