package koushur.kashmirievents.presentation.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import koushir.kashmirievents.R
import koushir.kashmirievents.databinding.ActivityMainBinding
import koushur.kashmirievents.presentation.base.BaseActivity
import koushur.kashmirievents.presentation.ui.main.calendar.LandingFragment
import koushur.kashmirievents.presentation.ui.main.featured.FeaturedFragment
import koushur.kashmirievents.presentation.ui.main.savedevents.SavedEventsFragment

class ActivityMain : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {
    private val landingFragment = LandingFragment()
    private val featuredFragment = FeaturedFragment()
    private val savedEvents = SavedEventsFragment()
    private var activeFragment: Fragment = landingFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpFragmentManager()
        viewBinding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.landing -> launchFragment(landingFragment)
                R.id.featured -> launchFragment(featuredFragment)
                R.id.saved_event -> launchFragment(savedEvents)
                else -> false
            }
        }
    }

    private fun setUpFragmentManager() {
        this.supportFragmentManager.beginTransaction().apply {
            add(
                R.id.fragment_container,
                featuredFragment,
                featuredFragment.javaClass.canonicalName
            ).hide(featuredFragment)
            add(
                R.id.fragment_container,
                savedEvents,
                savedEvents.javaClass.canonicalName
            ).hide(savedEvents)
            add(
                R.id.fragment_container,
                landingFragment,
                landingFragment.javaClass.canonicalName
            )
        }.commit()
    }

    private fun launchFragment(fragment: Fragment): Boolean {
        this.supportFragmentManager.beginTransaction().hide(activeFragment).show(fragment).commit()
        activeFragment = fragment
        return true
    }
}