package com.task.podcast.feature.mediaplayer.handler

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.util.Log
import java.io.IOException
import java.net.URL
import javax.inject.Inject

class MediaPlayerHandlerImpl @Inject constructor(): MediaPlayerHandler {
    private enum class PlayerState { IDLE, INITIALIZED, PREPARING, PREPARED, STARTED, PAUSED, STOPPED }

    companion object {
        private const val TAG = "MediaPlayerHandler"
        private const val NETWORK_TIMEOUT_MS = 15000L
        private const val MAX_RETRIES = 2
    }

    override var onPlaybackStarted: () -> Unit = {}
    override var onPlaybackCompleted: () -> Unit = {}
    override var onPlaybackError: (String) -> Unit = {}

    override var onPositionUpdate: (currentPosition: Int, duration: Int) -> Unit = { _, _ -> }

    private var currentUrl: String? = null
    private var player: MediaPlayer? = null
    private var playerState = PlayerState.IDLE
    private var validDuration = 0
    private var lastKnownPosition = 0
    private var retryCount = 0

    private val mainHandler = Handler(Looper.getMainLooper())
    private val playerLock = Any()

    private val timeoutRunnable = Runnable {
        synchronized(playerLock) {
            if (playerState == PlayerState.PREPARING) {
                handleError("Network timeout after ${NETWORK_TIMEOUT_MS}ms")
            }
        }
    }

    private var positionUpdateInterval = 1000L
    private val positionUpdater = object : Runnable {
        override fun run() {
            synchronized(playerLock) {
                if (playerState == PlayerState.STARTED) {
                    val current = getCurrentPosition()
                    val duration = validDuration
                    mainHandler.post {
                        onPositionUpdate(current, duration)
                    }
                    mainHandler.postDelayed(this, positionUpdateInterval)
                }
            }
        }
    }

    init {
        initializePlayer()
    }

    private fun initializePlayer() {
        synchronized(playerLock) {
            releasePlayerInternal()
            player = createNewPlayer().apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
                )
                setOnPreparedListener { onPrepared() }
                setOnCompletionListener { onCompletion() }
                setOnErrorListener { _, what, extra -> onPlayerError(what, extra) }
            }
            playerState = PlayerState.IDLE
            Log.d(TAG, "Player initialized")
        }
    }

    private fun createNewPlayer(): MediaPlayer {
        return MediaPlayer()
    }

    override fun startPlayback(audioSource: String) {
        synchronized(playerLock) {
            if (!isValidUrl(audioSource)) {
                handleError("Invalid URL format")
                return
            }
            if (playerState == PlayerState.PREPARING) {
                Log.w(TAG, "Already preparing - ignoring duplicate start")
                return
            }
            preparePlayback(audioSource)
        }
    }

    private fun preparePlayback(audioSource: String) {
        synchronized(playerLock) {
            try {
                if (currentUrl == audioSource && playerState == PlayerState.PAUSED) {
                    player?.start()
                    playerState = PlayerState.STARTED
                    onPlaybackStarted()
                    startPositionUpdates()
                } else {
                    player?.reset()
                    currentUrl = audioSource
                    validDuration = 0
                    lastKnownPosition = 0
                    retryCount = 0
                    playerState = PlayerState.INITIALIZED
                    try {
                        player?.setDataSource(audioSource)
                        playerState = PlayerState.PREPARING
                        player?.prepareAsync()
                        startNetworkTimeout()
                    } catch (e: IOException) {
                        handleError("Invalid data source: ${e.message}")
                    }
                }
            } catch (e: Exception) {
                handleError("Playback preparation failed: ${e.message}")
            }
        }
    }

    private fun onPrepared() {
        synchronized(playerLock) {
            mainHandler.removeCallbacks(timeoutRunnable)
            if (player == null) {
                handleError("Player instance lost during preparation")
                return
            }
            playerState = PlayerState.PREPARED
            validDuration = player?.duration ?: 0
            lastKnownPosition = 0
            runCatching {
                player?.start()
                playerState = PlayerState.STARTED
                onPlaybackStarted()
                startPositionUpdates()
            }.onFailure {
                handleError("Start failed after preparation: ${it.message}")
            }
        }
    }

    private fun onCompletion() {
        synchronized(playerLock) {
            playerState = PlayerState.STOPPED
            lastKnownPosition = validDuration
            onPlaybackCompleted()
            stopPositionUpdates()
            mainHandler.post { onPositionUpdate(validDuration, validDuration) }
        }
    }

    private fun onPlayerError(what: Int, extra: Int): Boolean {
        synchronized(playerLock) {
            handleError("MediaPlayer error: what=$what extra=$extra")
            return true
        }
    }

    override fun pausePlayback() {
        synchronized(playerLock) {
            if (playerState == PlayerState.STARTED) {
                runCatching {
                    player?.pause()
                    playerState = PlayerState.PAUSED
                }.onFailure {
                    handleError("Pause failed: ${it.message}")
                }
            } else {
                Log.w(TAG, "Cannot pause in state $playerState")
            }
            stopPositionUpdates()
        }
    }

    override fun seekTo(position: Int) {
        synchronized(playerLock) {
            if (playerState == PlayerState.PREPARED || playerState == PlayerState.STARTED || playerState == PlayerState.PAUSED) {
                runCatching {
                    val safePosition = position.coerceIn(0, validDuration)
                    player?.seekTo(safePosition)
                    lastKnownPosition = safePosition
                }.onFailure {
                    handleError("Seek failed: ${it.message}")
                }
            } else {
                Log.w(TAG, "Cannot seek in state $playerState")
            }
        }
    }

    override fun getDuration(): Int = synchronized(playerLock) { validDuration }

    fun getCurrentPosition(): Int = synchronized(playerLock) {
        when (playerState) {
            PlayerState.STARTED, PlayerState.PAUSED -> {
                player?.currentPosition?.also { lastKnownPosition = it } ?: lastKnownPosition
            }

            else -> lastKnownPosition
        }
    }

    override fun releasePlayer() {
        synchronized(playerLock) {
            stopPositionUpdates()
            mainHandler.removeCallbacks(timeoutRunnable)
            player?.release()
            player = null
            playerState = PlayerState.IDLE
            currentUrl = null
            validDuration = 0
            lastKnownPosition = 0
            retryCount = 0
            Log.d(TAG, "Player fully released")
        }
    }

    private fun releasePlayerInternal() {
        player?.release()
        player = null
        playerState = PlayerState.IDLE
    }

    private fun startNetworkTimeout() {
        mainHandler.removeCallbacks(timeoutRunnable)
        mainHandler.postDelayed(timeoutRunnable, NETWORK_TIMEOUT_MS)
    }

    private fun startPositionUpdates() {
        mainHandler.removeCallbacks(positionUpdater)
        if (playerState == PlayerState.STARTED) {
            mainHandler.post(positionUpdater)
        }
    }

    private fun stopPositionUpdates() {
        mainHandler.removeCallbacks(positionUpdater)
    }

    override fun setPositionUpdateInterval(intervalMs: Long) {
        positionUpdateInterval = intervalMs.coerceAtLeast(100)
    }

    private fun isValidUrl(url: String): Boolean = runCatching {
        URL(url).toURI()
        true
    }.getOrDefault(false)

    private fun isNetworkError(message: String): Boolean {
        return message.contains("timeout", true) ||
                message.contains("network", true) ||
                message.contains("http", true)
    }

    private fun handleError(message: String) {
        synchronized(playerLock) {
            Log.e(TAG, "Error: $message")
            mainHandler.post { onPlaybackError(message) }

            if (isNetworkError(message) && retryCount < MAX_RETRIES) {
                retryCount++
                Log.w(TAG, "Retrying (attempt $retryCount)...")
                currentUrl?.let { preparePlayback(it) }
            } else {
                releasePlayerInternal()
                initializePlayer()
            }
            stopPositionUpdates()
        }
    }
}