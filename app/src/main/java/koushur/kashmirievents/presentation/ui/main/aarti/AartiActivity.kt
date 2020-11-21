package koushur.kashmirievents.presentation.ui.main.aarti

import android.os.Bundle
import koushir.kashmirievents.R
import koushir.kashmirievents.databinding.ActivityAartiBinding
import koushur.kashmirievents.data.Aarti
import koushur.kashmirievents.presentation.base.BaseActivity

class AartiActivity : BaseActivity<ActivityAartiBinding>(R.layout.activity_aarti) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setHomeButtonEnabled(true)
        intent.extras?.getParcelable<Aarti>("aartiString")?.let {
            viewBinding.aarti = it
        }
    }
}