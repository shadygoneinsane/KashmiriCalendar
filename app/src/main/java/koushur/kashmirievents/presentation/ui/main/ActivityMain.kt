package koushur.kashmirievents.presentation.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import dagger.android.support.DaggerAppCompatActivity
import koushir.kashmirievents.R

class ActivityMain : DaggerAppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragmentManager = this.supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        val fragment: Fragment = LandingFragment()
        transaction.replace(
            R.id.fragment_container,
            fragment,
            fragment.javaClass.canonicalName
        )
        transaction.commit()
    }
}