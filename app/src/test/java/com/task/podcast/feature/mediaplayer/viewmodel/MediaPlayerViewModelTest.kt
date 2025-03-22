package com.task.podcast.feature.mediaplayer.viewmodel

import com.task.podcast.feature.mediaplayer.handler.MediaPlayerHandler
import com.task.podcast.feature.mediaplayer.model.MediaPlayerIntent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@OptIn(ExperimentalCoroutinesApi::class)
class MediaPlayerViewModelTest {

    private lateinit var testDispatcher: TestDispatcher
    private lateinit var testScheduler: TestCoroutineScheduler

    private lateinit var fakeMediaPlayerHandler: FakeMediaPlayerHandler
    private lateinit var viewModel: MediaPlayerViewModel

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        testScheduler = TestCoroutineScheduler()
        testDispatcher = StandardTestDispatcher(testScheduler)
        fakeMediaPlayerHandler = FakeMediaPlayerHandler()
        viewModel = MediaPlayerViewModel(fakeMediaPlayerHandler, testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when playback started, uiState isPlaying is true`() = runTest {
        fakeMediaPlayerHandler.onPlaybackStarted()
        testDispatcher.scheduler.advanceUntilIdle()
        val state = viewModel.uiState.first()
        assertTrue("Expected isPlaying to be true", state.isPlaying)
    }

    @Test
    fun `when playback completed, uiState is updated correctly`() = runTest {
        fakeMediaPlayerHandler.onPlaybackCompleted()
        testDispatcher.scheduler.advanceUntilIdle()
        val state = viewModel.uiState.first()
        assertFalse("Expected isPlaying to be false", state.isPlaying)
        assertEquals("Expected progress to be 0f", 0f, state.currentProgress)
        assertEquals(
            "Expected formattedCurrentTime to be 00:00", "00:00", state.formattedCurrentTime
        )
    }

    @Test
    fun `when playback error occurs, uiState isPlaying is false`() = runTest {
        fakeMediaPlayerHandler.onPlaybackError(Mockito.anyString())
        testDispatcher.scheduler.advanceUntilIdle()
        val state = viewModel.uiState.first()
        assertFalse("Expected isPlaying to be false on error", state.isPlaying)
    }

    @Test
    fun `onPositionUpdate callback updates progress and formatted times`() = runTest {
        // Given a position and duration
        val positionMs = 30_000
        val durationMs = 120_000

        fakeMediaPlayerHandler.onPositionUpdate(positionMs, durationMs)
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.first()
        // Verify progress is computed correctly
        val expectedProgress = positionMs.toFloat() / durationMs.toFloat()
        assertEquals(expectedProgress, state.currentProgress, 0.001f)
        // Verify formatted times
        assertEquals("00:30", state.formattedCurrentTime)
        assertEquals("02:00", state.formattedTotalTime)
        assertEquals(durationMs, state.durationMs)
    }

    @Test
    fun `onIntent Play triggers playback and updates uiState`() = runTest {
        // Given a Play intent
        val mediaUrl = "http://example.com/audio.mp3"
        val title = "Test Podcast"
        val imageUrl = "http://example.com/image.jpg"

        fakeMediaPlayerHandler.fakeDuration = 180_000 // 3 minutes

        viewModel.onIntent(MediaPlayerIntent.Play(mediaUrl, title, imageUrl))
        testDispatcher.scheduler.advanceUntilIdle()

        // Verify that startPlayback was called with the correct URL
        assertEquals(mediaUrl, fakeMediaPlayerHandler.lastStartedUrl)

        val state = viewModel.uiState.first()
        assertTrue(state.isPlaying)
        assertEquals(title, state.title)
        assertEquals(imageUrl, state.imageUrl)
        assertEquals(0f, state.currentProgress)
        assertEquals("00:00", state.formattedCurrentTime)
        assertEquals("03:00", state.formattedTotalTime)
        assertEquals(180_000, state.durationMs)

        // Verify that the player becomes visible
        assertTrue(viewModel.isPlayerVisible.first())
    }

    @Test
    fun `onIntent Pause triggers pausePlayback and updates uiState`() = runTest {
        fakeMediaPlayerHandler.onPlaybackStarted()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onIntent(MediaPlayerIntent.Pause)
        testDispatcher.scheduler.advanceUntilIdle()

        // Verify that pausePlayback was invoked on the handler
        assertTrue(fakeMediaPlayerHandler.pauseCalled)
        // Verify UI state
        val state = viewModel.uiState.first()
        assertFalse(state.isPlaying)
    }

    @Test
    fun `onIntent Seek triggers seekTo and updates formattedCurrentTime`() = runTest {
        // Given a seek intent to position 45 seconds (45000ms)
        val seekPosition = 45_000
        viewModel.onIntent(MediaPlayerIntent.Seek(seekPosition))
        testDispatcher.scheduler.advanceUntilIdle()

        // Verify that seekTo was invoked with the correct value
        assertEquals(seekPosition, fakeMediaPlayerHandler.lastSeekPosition)
        // Verify that formattedCurrentTime is updated
        val state = viewModel.uiState.first()
        assertEquals("00:45", state.formattedCurrentTime)
    }

    @Test
    fun `hidePlayer pauses playback and hides the player`() = runTest {
        viewModel.onIntent(MediaPlayerIntent.Play("url", "title", "image"))
        testDispatcher.scheduler.advanceUntilIdle()
        viewModel.hidePlayer()
        testDispatcher.scheduler.advanceUntilIdle()
        // Verify that pause was called
        assertTrue(fakeMediaPlayerHandler.pauseCalled)
        // Verify that the player visibility state is false
        assertFalse(viewModel.isPlayerVisible.first())
    }
}

class FakeMediaPlayerHandler : MediaPlayerHandler {

    override var onPlaybackStarted: () -> Unit = {}
    override var onPlaybackCompleted: () -> Unit = {}
    override var onPlaybackError: (String) -> Unit = {}
    override var onPositionUpdate: (currentPosition: Int, duration: Int) -> Unit = { _, _ -> }

    var fakeDuration: Int = 0

    override fun startPlayback(audioSource: String) {
        lastStartedUrl = audioSource
        onPlaybackStarted()
    }

    override fun pausePlayback() {
        pauseCalled = true
    }

    override fun seekTo(position: Int) {
        lastSeekPosition = position
    }

    override fun getDuration(): Int = fakeDuration

    override fun releasePlayer() {}

    override fun setPositionUpdateInterval(intervalMs: Long) {}

    var lastStartedUrl: String? = null
    var pauseCalled = false
    var lastSeekPosition: Int? = null
}