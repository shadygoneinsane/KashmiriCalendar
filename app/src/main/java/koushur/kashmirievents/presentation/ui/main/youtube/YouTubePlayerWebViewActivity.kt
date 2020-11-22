package koushur.kashmirievents.presentation.ui.main.youtube

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import koushir.kashmirievents.R
import koushir.kashmirievents.databinding.ActivityYoutubeWebViewLayoutBinding
import koushur.kashmirievents.data.VideoData
import koushur.kashmirievents.presentation.base.BaseActivity
import koushur.kashmirievents.presentation.utils.AppConstants
import koushur.kashmirievents.presentation.utils.toast

/**
 * A [YouTubePlayerWebViewActivity] for playing videos from Youtube
 *
 * Author: Vikesh Dass
 * Created on: 21-11-2020
 * Email : vikeshdass@gmail.com
 */
class YouTubePlayerWebViewActivity :
    BaseActivity<ActivityYoutubeWebViewLayoutBinding>(R.layout.activity_youtube_web_view_layout) {
    private var videoData: VideoData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        videoData = intent.getParcelableExtra(AppConstants.YOUTUBE_VIDEO_ID)

        viewBinding.videoData = videoData
        lifecycle.addObserver(viewBinding.youtubePlayerWebView)
        viewBinding.progressBar.visibility = View.VISIBLE
        viewBinding.youtubePlayerWebView.addYouTubePlayerListener(object :
            AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer) {
                super.onReady(youTubePlayer)
                viewBinding.progressBar.visibility = View.GONE
                videoData?.videoId?.let { videoId ->
                    youTubePlayer.loadVideo(videoId, 0f)
                }
            }

            override fun onError(
                youTubePlayer: com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer,
                error: PlayerConstants.PlayerError
            ) {
                super.onError(youTubePlayer, error)
                viewBinding.progressBar.visibility = View.GONE
                "Unknown error".toast(baseContext, Toast.LENGTH_LONG)
            }
        })
    }
}
