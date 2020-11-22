package koushur.kashmirievents.presentation.ui.main.featured

import android.content.Context
import androidx.databinding.ObservableArrayList
import koushir.kashmirievents.BR
import koushir.kashmirievents.BuildConfig
import koushir.kashmirievents.R
import koushur.kashmirievents.data.AartiData
import koushur.kashmirievents.data.VideoData
import koushur.kashmirievents.interfaces.OnOptionClickListener
import koushur.kashmirievents.network.remote.RemoteConfigManager
import koushur.kashmirievents.presentation.base.BaseViewModel
import koushur.kashmirievents.presentation.navigation.SingleLiveEvent
import koushur.kashmirievents.presentation.utils.AppConstants
import me.tatarka.bindingcollectionadapter2.itembindings.OnItemBindClass

class FeaturedViewModel(
    private val context: Context,
    val remoteConfigManager: RemoteConfigManager
) : BaseViewModel() {
    val clickListener = SingleLiveEvent<AartiData>()
    val videoClickListener = SingleLiveEvent<VideoData>()

    private val youtubeApiKey: String? = BuildConfig.API_KEY
    fun getYoutubeApiKey(): String? {
        return youtubeApiKey
    }

    private val videoIdSukhTaSampada: String? =
        remoteConfigManager.getVideoId(AppConstants.ID_VIDEO_SUKH_TA_SAMPADA)

    private val videoIdBaMajeyKeh: String? =
        remoteConfigManager.getVideoId(AppConstants.ID_VIDEO_BA_MAJEY_KEH_NE_ZANAY)

    private val videoIdMaajSharikay: String? =
        remoteConfigManager.getVideoId(AppConstants.ID_VIDEO_MAAJ_SHARIKAY_KAR_DAYA)

    val aartiItems = ObservableArrayList<Any>()
    val aartiItemBinding: OnItemBindClass<Any> = OnItemBindClass<Any>()
        .map(AartiData::class.java) { itemBinding, _, _ ->
            itemBinding
                .clearExtras()
                .set(BR.aarti, R.layout.layout_list_aartis)
                .bindExtra(BR.clickListener, object : OnOptionClickListener<AartiData> {
                    override fun onOptionClick(option: AartiData) {
                        clickListener.value = option
                    }
                })
        }
        .map(VideoData::class.java) { itemBinding, _, _ ->
            itemBinding
                .clearExtras()
                .set(BR.videoData, R.layout.layout_list_video)
                .bindExtra(BR.clickListener, object : OnOptionClickListener<VideoData> {
                    override fun onOptionClick(option: VideoData) {
                        videoClickListener.value = option
                    }
                })
        }

    fun setAartiData() {
        videoIdSukhTaSampada?.let {
            aartiItems.add(VideoData(context.getString(R.string.leela_sukh_ta_sampada), it))
        }
        videoIdBaMajeyKeh?.let {
            aartiItems.add(VideoData(context.getString(R.string.leela_ba_majey), it))
        }
        videoIdMaajSharikay?.let {
            aartiItems.add(VideoData(context.getString(R.string.leela_maaj_sharikay), it))
        }
        aartiItems.add(
            AartiData(
                context.getString(R.string.title_eng_ganeshaarti),
                context.getString(R.string.title_ganeshaarti),
                context.getString(R.string.item_ganeshaarti)
            )
        )
        aartiItems.add(
            AartiData(
                context.getString(R.string.title_eng_ambeymataaarti),
                context.getString(R.string.title_ambeymataaarti),
                context.getString(R.string.item_ambeymataaarti)
            )
        )
        aartiItems.add(
            AartiData(
                context.getString(R.string.title_eng_durgemataaarti),
                context.getString(R.string.title_durgemataaarti),
                context.getString(R.string.item_durgemataaarti)
            )
        )
        aartiItems.add(
            AartiData(
                context.getString(R.string.title_eng_hanumanaarti),
                context.getString(R.string.title_hanumanaarti),
                context.getString(R.string.item_hanumanaarti)
            )
        )
        aartiItems.add(
            AartiData(
                context.getString(R.string.title_eng_krishnaaarti),
                context.getString(R.string.title_krishnaaarti),
                context.getString(R.string.item_krishnaaarti)
            )
        )
        aartiItems.add(
            AartiData(
                context.getString(R.string.title_eng_vashnodevimataaarti),
                context.getString(R.string.title_vashnodevimataaarti),
                context.getString(R.string.item_vashnodevimataaarti)
            )
        )
        aartiItems.add(
            AartiData(
                context.getString(R.string.title_eng_saraswatiimataaarti),
                context.getString(R.string.title_saraswatiimataaarti),
                context.getString(R.string.item_saraswatiimataaarti)
            )
        )
        aartiItems.add(
            AartiData(
                context.getString(R.string.title_eng_saiaarti),
                context.getString(R.string.title_saiaarti),
                context.getString(R.string.item_saiaarti)
            )
        )
        aartiItems.add(
            AartiData(
                context.getString(R.string.title_eng_shivaarti),
                context.getString(R.string.title_shivaarti),
                context.getString(R.string.item_shivaarti)
            )
        )
        aartiItems.add(
            AartiData(
                context.getString(R.string.title_eng_jagdishaarti),
                context.getString(R.string.title_jagdishaarti),
                context.getString(R.string.item_jagdishaarti)
            )
        )
        aartiItems.add(
            AartiData(
                context.getString(R.string.title_eng_laxmimataaarti),
                context.getString(R.string.title_laxmimataaarti),
                context.getString(R.string.item_laxmimataaarti)
            )
        )
        aartiItems.add(
            AartiData(
                context.getString(R.string.title_eng_santoshimataaarti),
                context.getString(R.string.title_santoshimataaarti),
                context.getString(R.string.item_santoshimataaarti)
            )
        )
    }
}