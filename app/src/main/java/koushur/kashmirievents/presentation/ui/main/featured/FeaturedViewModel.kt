package koushur.kashmirievents.presentation.ui.main.featured

import android.content.Context
import androidx.databinding.ObservableArrayList
import koushir.kashmirievents.BR
import koushir.kashmirievents.R
import koushur.kashmirievents.data.Aarti
import koushur.kashmirievents.interfaces.OnOptionClickListener
import koushur.kashmirievents.presentation.base.BaseViewModel
import koushur.kashmirievents.presentation.navigation.SingleLiveEvent
import me.tatarka.bindingcollectionadapter2.itembindings.OnItemBindClass

class FeaturedViewModel(private val context: Context) : BaseViewModel() {
    val clickListener = SingleLiveEvent<Aarti>()

    val aartiItems = ObservableArrayList<Any>()
    val aartiItemBinding: OnItemBindClass<Any> = OnItemBindClass<Any>()
        .map(Aarti::class.java) { itemBinding, _, _ ->
            itemBinding
                .clearExtras()
                .set(BR.aarti, R.layout.layout_list_aartis)
                .bindExtra(BR.clickListener, object : OnOptionClickListener<Aarti> {
                    override fun onOptionClick(option: Aarti) {
                        clickListener.value = option
                    }
                })
        }

    fun setAartiData() {
        aartiItems.add(
            Aarti(
                context.getString(R.string.title_eng_ganeshaarti),
                context.getString(R.string.title_ganeshaarti),
                context.getString(R.string.item_ganeshaarti)
            )
        )
        aartiItems.add(
            Aarti(
                context.getString(R.string.title_eng_ambeymataaarti),
                context.getString(R.string.title_ambeymataaarti),
                context.getString(R.string.item_ambeymataaarti)
            )
        )
        aartiItems.add(
            Aarti(
                context.getString(R.string.title_eng_durgemataaarti),
                context.getString(R.string.title_durgemataaarti),
                context.getString(R.string.item_durgemataaarti)
            )
        )
        aartiItems.add(
            Aarti(
                context.getString(R.string.title_eng_hanumanaarti),
                context.getString(R.string.title_hanumanaarti),
                context.getString(R.string.item_hanumanaarti)
            )
        )
        aartiItems.add(
            Aarti(
                context.getString(R.string.title_eng_krishnaaarti),
                context.getString(R.string.title_krishnaaarti),
                context.getString(R.string.item_krishnaaarti)
            )
        )
        aartiItems.add(
            Aarti(
                context.getString(R.string.title_eng_vashnodevimataaarti),
                context.getString(R.string.title_vashnodevimataaarti),
                context.getString(R.string.item_vashnodevimataaarti)
            )
        )
        aartiItems.add(
            Aarti(
                context.getString(R.string.title_eng_saraswatiimataaarti),
                context.getString(R.string.title_saraswatiimataaarti),
                context.getString(R.string.item_saraswatiimataaarti)
            )
        )
        aartiItems.add(
            Aarti(
                context.getString(R.string.title_eng_saiaarti),
                context.getString(R.string.title_saiaarti),
                context.getString(R.string.item_saiaarti)
            )
        )
        aartiItems.add(
            Aarti(
                context.getString(R.string.title_eng_shivaarti),
                context.getString(R.string.title_shivaarti),
                context.getString(R.string.item_shivaarti)
            )
        )
        aartiItems.add(
            Aarti(
                context.getString(R.string.title_eng_jagdishaarti),
                context.getString(R.string.title_jagdishaarti),
                context.getString(R.string.item_jagdishaarti)
            )
        )
        aartiItems.add(
            Aarti(
                context.getString(R.string.title_eng_laxmimataaarti),
                context.getString(R.string.title_laxmimataaarti),
                context.getString(R.string.item_laxmimataaarti)
            )
        )
        aartiItems.add(
            Aarti(
                context.getString(R.string.title_eng_santoshimataaarti),
                context.getString(R.string.title_santoshimataaarti),
                context.getString(R.string.item_santoshimataaarti)
            )
        )
    }
}