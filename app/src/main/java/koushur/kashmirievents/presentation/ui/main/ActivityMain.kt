package koushur.kashmirievents.presentation.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import koushir.kashmirievents.R
import koushir.kashmirievents.databinding.ActivityMainBinding
import koushur.kashmirievents.presentation.ui.main.calendar.LandingFragment
import koushur.kashmirievents.presentation.ui.main.featured.FeaturedFragment

class ActivityMain : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val landingFragment = LandingFragment()
    private val featuredFragment = FeaturedFragment()
    private var activeFragment: Fragment = landingFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setUpFragmentManager()
        binding.bottomNav.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.landing -> launchFragment(landingFragment)
                R.id.featured -> launchFragment(featuredFragment)
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