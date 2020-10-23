package koushur.kashmirievents.presentation.ui.main

import android.graphics.Rect
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.Px
import androidx.recyclerview.widget.RecyclerView


/**
 * File Description
 * Created by: Vikesh Dass
 * Created on: 23-10-2020
 * Email : vikeshdass@gmail.com
 */
/**
 * A [RecyclerView.ItemDecoration] which is inset from the left by the given amount.
 */
class ItemDivider(
    @Px private val verticalSpace: Int,
    @ColorInt private val horizontalSpace: Int
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.top = verticalSpace / 2
        if (parent.getChildAdapterPosition(view) < state.itemCount - 1)
            outRect.bottom = verticalSpace / 2
    }
}
