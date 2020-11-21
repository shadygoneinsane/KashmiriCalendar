package koushur.kashmirievents.presentation.ui.main.youtube

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayer.*
import kotlinx.android.synthetic.main.activity_youtube_layout.*
import koushir.kashmirievents.R
import koushur.kashmirievents.presentation.utils.AppConstants
import timber.log.Timber

/**
 * A [YouTubePlayerActivity] for playing videos from Youtube
 *
 * Author: Vikesh Dass
 * Created on: 21-11-2020
 * Email : vikeshdass@gmail.com
 */
class YouTubePlayerActivity : YouTubeBaseActivity(), OnInitializedListener {
    private var player: YouTubePlayer? = null
    private var videoId: String? = null
    private var playerStateChangeListener: MyPlayerStateChangeListener? = null
    private var playbackEventListener: MyPlaybackEventListener? = null

    override fun onCreate(p0: Bundle?) {
        super.onCreate(p0)
        setContentView(R.layout.activity_youtube_layout)

        videoId = intent.getStringExtra(AppConstants.VIDEO_ID)

        intent.getStringExtra(AppConstants.YOUTUBE_API_KEY)?.let {
            youtube_view.initialize(it, this)
            progressBar.visibility = View.VISIBLE
            playerStateChangeListener = MyPlayerStateChangeListener()
            playbackEventListener = MyPlaybackEventListener()
        }
    }

    override fun onInitializationSuccess(
        p0: YouTubePlayer.Provider?,
        player: YouTubePlayer?,
        p2: Boolean
    ) {
        this.player = player
        progressBar.visibility = View.GONE
        player?.cueVideo(videoId)
        player?.setPlayerStyle(PlayerStyle.DEFAULT)

        player?.setPlayerStateChangeListener(playerStateChangeListener)
        player?.setPlaybackEventListener(playbackEventListener)
    }

    override fun onInitializationFailure(
        p0: YouTubePlayer.Provider?,
        p1: YouTubeInitializationResult?
    ) {
        val errorMessage: String = p1.toString()
        Toast.makeText(baseContext, errorMessage, Toast.LENGTH_LONG).show()
    }

    inner class MyPlaybackEventListener : PlaybackEventListener {
        var playbackState = "NOT_PLAYING"
        var bufferingState = ""
        override fun onPlaying() {
            playbackState = "PLAYING"

            Timber.d("\tPLAYING ")
        }

        override fun onBuffering(isBuffering: Boolean) {
            bufferingState = if (isBuffering) "(BUFFERING)" else ""

            Timber.d(if (isBuffering) "BUFFERING " else "NOT BUFFERING ")
        }

        override fun onStopped() {
            playbackState = "STOPPED"

            Timber.d("\tSTOPPED")
        }

        override fun onPaused() {
            playbackState = "PAUSED"

            Timber.d("\tPAUSED ")
        }

        override fun onSeekTo(endPositionMillis: Int) {

        }
    }

    inner class MyPlayerStateChangeListener : PlayerStateChangeListener {
        var playerState = "UNINITIALIZED"
        override fun onLoading() {
            playerState = "LOADING"

            Timber.d(playerState)
        }

        override fun onLoaded(videoId: String) {
            playerState = String.format("LOADED %s", videoId)
            player?.play()
            Timber.d(playerState)
        }

        override fun onAdStarted() {
            playerState = "AD_STARTED"

            Timber.d(playerState)
        }

        override fun onVideoStarted() {
            playerState = "VIDEO_STARTED"

            Timber.d(playerState)
        }

        override fun onVideoEnded() {
            playerState = "VIDEO_ENDED"

            Timber.d(playerState)
        }

        override fun onError(reason: ErrorReason) {
            playerState = "ERROR ($reason)"
            if (reason == ErrorReason.UNEXPECTED_SERVICE_DISCONNECTION) {
                // When this error occurs the player is released and can no longer be used.
            }

            Timber.d(playerState)
        }
    }
}
