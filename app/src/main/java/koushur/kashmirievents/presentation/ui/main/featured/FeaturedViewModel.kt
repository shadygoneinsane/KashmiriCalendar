package koushur.kashmirievents.presentation.ui.main.featured

import android.content.Context
import androidx.databinding.ObservableArrayList
import koushir.kashmirievents.BR
import koushir.kashmirievents.R
import koushur.kashmirievents.presentation.base.BaseViewModel
import me.tatarka.bindingcollectionadapter2.itembindings.OnItemBindClass

class FeaturedViewModel(private val context: Context) : BaseViewModel() {
    val aartiItems = ObservableArrayList<Any>()
    val aartiItemBinding: OnItemBindClass<Any> = OnItemBindClass<Any>()
        .map(Aarti::class.java) { itemBinding, _, _ ->
            itemBinding.clearExtras().set(BR.aarti, R.layout.layout_item_aarti)
        }

    data class Aarti(val id: Int, val aartiString: String)

    fun setAartiData() {
        aartiItems.add(Aarti(1, context.getString(R.string.item_ganeshaarti)))
    }
}