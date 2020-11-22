package koushur.kashmirievents.presentation.ui.main.youtube

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import koushir.kashmirievents.R
import koushir.kashmirievents.databinding.ActivityYoutubeWebViewLayoutBinding
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(viewBinding.youtubePlayerWebView)
        intent.getStringExtra(AppConstants.YOUTUBE_API_KEY)?.let {
            viewBinding.progressBar.visibility = View.VISIBLE
        }

        viewBinding.youtubePlayerWebView.visibility = View.VISIBLE
        viewBinding.youtubePlayerWebView.addYouTubePlayerListener(object :
            AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer) {
                super.onReady(youTubePlayer)
                viewBinding.progressBar.visibility = View.GONE
                intent.getStringExtra(AppConstants.VIDEO_ID)?.let { videoId ->
                    youTubePlayer.loadVideo(videoId, 0f)
                }
            }

            override fun onError(
                youTubePlayer: com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer,
                error: PlayerConstants.PlayerError
            ) {
                super.onError(youTubePlayer, error)
                viewBinding.progressBar.visibility = View.GONE
                when (error) {
                    PlayerConstants.PlayerError.UNKNOWN -> {
                        "Unknown error".toast(baseContext, Toast.LENGTH_LONG)
                    }
                    else -> {
                        "Unknown error".toast(baseContext, Toast.LENGTH_LONG)
                    }
                }
            }
        })
    }
}
