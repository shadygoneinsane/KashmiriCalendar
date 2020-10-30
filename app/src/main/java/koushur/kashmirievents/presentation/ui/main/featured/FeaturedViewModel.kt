package koushur.kashmirievents.presentation.ui.main.featured

import android.content.Context
import androidx.databinding.ObservableArrayList
import koushir.kashmirievents.BR
import koushir.kashmirievents.R
import koushur.kashmirievents.presentation.base.BaseViewModel
import me.tatarka.bindingcollectionadapter2.BindingViewPagerAdapter
import me.tatarka.bindingcollectionadapter2.itembindings.OnItemBindClass

class FeaturedViewModel(private val context: Context) : BaseViewModel() {
    val aartiItems = ObservableArrayList<Any>()
    val aartiItemBinding: OnItemBindClass<Any> = OnItemBindClass<Any>()
        .map(Aarti::class.java) { itemBinding, _, _ ->
            itemBinding.clearExtras().set(BR.aarti, R.layout.layout_item_aarti)
        }

    data class Aarti(val title: String, val aartiString: String)

    fun setAartiData() {
        aartiItems.add(Aarti("Sri GaneshJi Aarti", context.getString(R.string.item_ganeshaarti)))
        aartiItems.add(Aarti("Ambey Mata Aarti", context.getString(R.string.item_ambeymataaarti)))
        aartiItems.add(Aarti("Durga Mata Aarti", context.getString(R.string.item_durgemataaarti)))
        aartiItems.add(Aarti("Sri Hanuman Aarti", context.getString(R.string.item_hanumanaarti)))
        aartiItems.add(
            Aarti(
                "Mata Vaishnodevi Aarti",
                context.getString(R.string.item_vashnodevimataaarti)
            )
        )
        aartiItems.add(
            Aarti(
                "Saraswati Mata Aarti",
                context.getString(R.string.item_saraswatiimataaarti)
            )
        )
        aartiItems.add(Aarti("Saibaba Aarti", context.getString(R.string.item_saiaarti)))
    }

    /**
     * Define page titles for a ViewPager
     */
    val pageTitles =
        BindingViewPagerAdapter.PageTitles<Aarti> { _, item -> item.title }
}