package koushur.kashmirievents.presentation.ui.main.aarti

import android.os.Bundle
import koushir.kashmirievents.R
import koushir.kashmirievents.databinding.ActivityAartiBinding
import koushur.kashmirievents.database.data.AartiData
import koushur.kashmirievents.presentation.base.BaseActivity
import koushur.kashmirievents.utility.AppConstants

class AartiActivity : BaseActivity<ActivityAartiBinding>(R.layout.activity_aarti) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setHomeButtonEnabled(true)
        intent.extras?.getParcelable<AartiData>(AppConstants.AARTI_STRING)?.let {
            viewBinding.aarti = it
        }
    }
}