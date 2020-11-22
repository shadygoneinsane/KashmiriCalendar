package koushur.kashmirievents.presentation.ui.main.featured

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.youtube.player.YouTubeIntents
import koushir.kashmirievents.R
import koushir.kashmirievents.databinding.FragmentFeaturedBinding
import koushur.kashmirievents.presentation.base.BaseFragment
import koushur.kashmirievents.presentation.base.BaseViewModel
import koushur.kashmirievents.presentation.ui.main.aarti.AartiActivity
import koushur.kashmirievents.presentation.ui.main.calendar.InsetDivider
import koushur.kashmirievents.presentation.ui.main.youtube.YouTubePlayerActivity
import koushur.kashmirievents.presentation.ui.main.youtube.YouTubePlayerWebViewActivity
import koushur.kashmirievents.presentation.utils.AppConstants
import koushur.kashmirievents.presentation.utils.toast
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * A [BaseFragment] for showing featured items in a list
 *
 * Author: Vikesh Dass
 * Created on: 29-10-2020
 * Email : vikeshdass@gmail.com
 */
class FeaturedFragment : BaseFragment<FragmentFeaturedBinding>(R.layout.fragment_featured) {

    private val viewModel: FeaturedViewModel by viewModel()

    override fun provideViewModel(): BaseViewModel = viewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.clickListener.observe(this, {
            val intent = Intent(activity, AartiActivity::class.java)
            intent.putExtra("aartiString", it)
            startActivity(intent)
        })

        viewModel.videoClickListener.observe(this, {
            YouTubeIntents.getInstalledYouTubeVersionName(context)?.let { version ->
                String.format(getString(R.string.youtube_currently_installed), version)
                    .toast(context)
                openYoutubeActivity()
            } ?: run {
                getString(R.string.youtube_not_installed).toast(context)
                openYoutubeWebViewActivity()
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.viewModel = viewModel
        viewModel.setAartiData()
        viewBinding.listAartis.addItemDecoration(
            InsetDivider(
                resources.getDimensionPixelSize((R.dimen.inset_height)),
                resources.getDimensionPixelSize((R.dimen.divider_height)),
                ContextCompat.getColor(viewBinding.listAartis.context, R.color.green_200)
            )
        )
    }

    private fun openYoutubeActivity() {
        val intent = Intent(activity, YouTubePlayerActivity::class.java)
        val bundle = Bundle().apply {
            putString(AppConstants.YOUTUBE_API_KEY, viewModel.getYoutubeApiKey())
            putString(AppConstants.VIDEO_ID, viewModel.getVideoId())
        }

        startActivity(intent.apply {
            putExtras(bundle)
        })
    }

    private fun openYoutubeWebViewActivity() {
        val intent = Intent(activity, YouTubePlayerWebViewActivity::class.java)
        val bundle = Bundle().apply {
            putString(AppConstants.YOUTUBE_API_KEY, viewModel.getYoutubeApiKey())
            putString(AppConstants.VIDEO_ID, viewModel.getVideoId())
        }

        startActivity(intent.apply {
            putExtras(bundle)
        })
    }
}