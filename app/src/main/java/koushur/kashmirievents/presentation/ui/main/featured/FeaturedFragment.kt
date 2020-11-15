package koushur.kashmirievents.presentation.ui.main.featured

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import koushir.kashmirievents.R
import koushir.kashmirievents.databinding.FragmentFeaturedBinding
import koushur.kashmirievents.presentation.base.BaseFragment
import koushur.kashmirievents.presentation.base.BaseViewModel
import koushur.kashmirievents.presentation.ui.main.aartiactivity.AartiActivity
import koushur.kashmirievents.presentation.ui.main.calendar.InsetDivider
import org.koin.android.viewmodel.ext.android.viewModel

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
}