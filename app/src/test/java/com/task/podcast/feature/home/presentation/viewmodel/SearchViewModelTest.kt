package com.task.podcast.feature.home.presentation.viewmodel

import com.task.podcast.feature.home.data.fake.FakeData
import com.task.podcast.feature.home.domain.usecase.SearchUseCase
import com.task.podcast.feature.home.presentation.model.SearchIntent
import com.task.podcast.feature.home.presentation.model.SearchUiEvent
import com.task.podcast.feature.home.presentation.model.SearchUiState
import com.task.podcast.feature.home.presentation.model.SectionUiModel
import com.task.podcast.feature.home.presentation.model.mapper.toUiModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
class SearchViewModelMockitoTest {

    @Mock
    private lateinit var searchUseCase: SearchUseCase

    private lateinit var viewModel: SearchViewModel
    private lateinit var testDispatcher: TestDispatcher
    private lateinit var testScheduler: TestCoroutineScheduler

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        testScheduler = TestCoroutineScheduler()
        testDispatcher = StandardTestDispatcher(testScheduler)
        viewModel = SearchViewModel(
            searchUseCase = searchUseCase,
            mainDispatcher = testDispatcher
        )
    }

    @Test
    fun `processIntent with blank query updates state with no results`() = runTest(testScheduler) {
        // When: Update with a blank query.
        viewModel.processIntent(SearchIntent.UpdateQuery(""))
        // Advance time until debounce and processing complete.
        advanceUntilIdle()
        // Then: Verify state.
        val state: SearchUiState = viewModel.uiState.value
        assertEquals("", state.query)
        assertFalse(state.isLoading)
        assertNull(state.searchResults)
    }

    @Test
    fun `processIntent with non blank query updates state with search results`() =
        runTest(testScheduler) {
            // Given: When "test" is passed, the use case returns a fake SectionEntity.
            val fakeSectionEntity = FakeData.getFakeSectionEntity(listOf(FakeData.getFakeContentEntity()))
            whenever(searchUseCase.invoke("test")).thenReturn(listOf(fakeSectionEntity))
            // When: Process the intent with query "test".
            viewModel.processIntent(SearchIntent.UpdateQuery("test"))
            // Advance time to ensure the debounce period elapses.
            advanceUntilIdle()
            // Then: Verify that the state is updated.
            val state: SearchUiState = viewModel.uiState.value
            assertEquals("test", state.query)
            assertFalse(state.isLoading)
            assertNotNull(state.searchResults)
            assertTrue(state.searchResults!!.isNotEmpty())
            // Verify that the mapped UI model has the expected section name.
            val expectedUiModel: SectionUiModel = fakeSectionEntity.toUiModel()
            assertEquals(expectedUiModel.sectionName, state.searchResults[0].sectionName)
        }

    @Test
    fun `processIntent when searchUseCase throws exception emits error event`() =
        runTest(testScheduler) {
            // Given: Configure the mock to throw an exception for the query "anything".
            whenever(searchUseCase.invoke("anything")).thenThrow(RuntimeException("Test exception"))
            // Collect emitted UI events.
            val emittedEvents = mutableListOf<Any>()
            val job = launch(testDispatcher) {
                viewModel.uiEvent.collect { emittedEvents.add(it) }
            }
            // When: Process the intent.
            viewModel.processIntent(SearchIntent.UpdateQuery("anything"))
            advanceUntilIdle()
            // Then: Assert that an error event was emitted.
            assertTrue(emittedEvents.any { it is SearchUiEvent.ShowError })
            job.cancel()
        }
}