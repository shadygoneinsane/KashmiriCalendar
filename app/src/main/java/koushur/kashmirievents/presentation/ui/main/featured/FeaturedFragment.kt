package koushur.kashmirievents.presentation.ui.main.featured

import android.os.Bundle
import android.view.View
import com.google.android.material.tabs.TabLayoutMediator
import koushir.kashmirievents.R
import koushir.kashmirievents.databinding.FragmentFeaturedBinding
import koushur.kashmirievents.presentation.base.BaseFragment
import koushur.kashmirievents.presentation.base.BaseViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class FeaturedFragment : BaseFragment<FragmentFeaturedBinding>(R.layout.fragment_featured) {

    private val viewModel: FeaturedViewModel by viewModel()

    override fun provideViewModel(): BaseViewModel = viewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.viewModel = viewModel
        viewModel.setAartiData()
    }

    override fun onResume() {
        super.onResume()
        TabLayoutMediator(viewBinding.tabLayout, viewBinding.viewPager) { tab, position ->
            val item = viewModel.aartiItems[position]
            tab.text = viewModel.pageTitles.getPageTitle(position, item as FeaturedViewModel.Aarti?)
        }.attach()
    }
}