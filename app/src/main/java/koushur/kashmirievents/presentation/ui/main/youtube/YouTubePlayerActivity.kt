package koushur.kashmirievents.presentation.ui.main.youtube

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayer.ErrorReason
import com.google.android.youtube.player.YouTubePlayer.OnInitializedListener
import com.google.android.youtube.player.YouTubePlayer.PlayerStateChangeListener
import com.google.android.youtube.player.YouTubePlayer.PlayerStyle
import com.google.android.youtube.player.YouTubePlayer.Provider
import koushir.kashmirievents.R
import koushir.kashmirievents.databinding.ActivityYoutubeLayoutBinding
import koushur.kashmirievents.database.data.VideoData
import koushur.kashmirievents.utility.AppConstants
import koushur.kashmirievents.utility.log
import koushur.kashmirievents.utility.toast

/**
 * A [YouTubePlayerActivity] for playing videos from Youtube
 *
 * Author: Vikesh Dass
 * Created on: 21-11-2020
 * Email : vikeshdass@gmail.com
 */
class YouTubePlayerActivity : YouTubeBaseActivity(), OnInitializedListener {
    private var player: YouTubePlayer? = null
    private var videoData: VideoData? = null
    private var playerStateChangeListener: MyPlayerStateChangeListener? = null
    private lateinit var viewBinding: ActivityYoutubeLayoutBinding

    override fun onCreate(p0: Bundle?) {
        super.onCreate(p0)
        viewBinding = DataBindingUtil.setContentView(this, R.layout.activity_youtube_layout)
        videoData = intent.getParcelableExtra(AppConstants.YOUTUBE_VIDEO_ID)
        viewBinding.videoData = videoData

        intent.getStringExtra(AppConstants.YOUTUBE_API_KEY)?.let {
            viewBinding.youtubeView.initialize(it, this)
            viewBinding.progressBar.visibility = View.VISIBLE
            playerStateChangeListener = MyPlayerStateChangeListener()
        }
    }

    override fun onInitializationSuccess(p0: Provider?, player: YouTubePlayer?, p2: Boolean) {
        this.player = player
        viewBinding.progressBar.visibility = View.GONE
        videoData?.videoId?.let { player?.cueVideo(it) }
        player?.setPlayerStyle(PlayerStyle.DEFAULT)

        player?.setPlayerStateChangeListener(playerStateChangeListener)
    }

    override fun onInitializationFailure(p0: Provider?, p1: YouTubeInitializationResult?) {
        val errorMessage: String = p1.toString()
        viewBinding.progressBar.visibility = View.GONE
        errorMessage.toast(baseContext, Toast.LENGTH_LONG)
    }

    inner class MyPlayerStateChangeListener : PlayerStateChangeListener {
        var playerState = "UNINITIALIZED"
        override fun onLoading() {
            playerState = "LOADING"

            log(playerState)
        }

        override fun onLoaded(videoId: String) {
            playerState = String.format("LOADED %s", videoId)
            player?.play()
            log(playerState)
        }

        override fun onAdStarted() {
            playerState = "AD_STARTED"

            log(playerState)
        }

        override fun onVideoStarted() {
            playerState = "VIDEO_STARTED"

            log(playerState)
        }

        override fun onVideoEnded() {
            playerState = "VIDEO_ENDED"

            log(playerState)
        }

        override fun onError(reason: ErrorReason) {
            playerState = "ERROR ($reason)"
            if (reason == ErrorReason.UNEXPECTED_SERVICE_DISCONNECTION) {
                // When this error occurs the player is released and can no longer be used.
            }

            log(playerState)
        }
    }
}
