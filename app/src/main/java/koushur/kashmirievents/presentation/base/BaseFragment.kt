package koushur.kashmirievents.presentation.base

import android.app.AlertDialog
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import koushir.kashmirievents.R


abstract class BaseFragment<VB : ViewDataBinding>(private val layoutId: Int) : Fragment() {
    abstract fun provideViewModel(): BaseViewModel?

    protected lateinit var viewBinding: VB
    private var baseViewModel: BaseViewModel? = null
    private lateinit var progressAlertDialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(layoutId, container, false)
        viewBinding = DataBindingUtil.bind(view)!!
        viewBinding.lifecycleOwner = this
        return view
    }

    /**
     * Method to observer loading
     */
    internal fun observeLoading(loadingInProgress: Boolean) {
        if (loadingInProgress) {
            showLoading()
        } else {
            hideLoading()
        }
    }

    protected fun showLoading() {
        hideLoading()
        val dialogBuilder = AlertDialog.Builder(context)
        val inflater = context?.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater?
        val dialogView = inflater?.inflate(R.layout.dialog_progress, null)
        dialogBuilder.setView(dialogView)
        dialogBuilder.setCancelable(false)
        progressAlertDialog = dialogBuilder.create()
        progressAlertDialog.show()
        progressAlertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

    }

    protected fun hideLoading() {
        if (::progressAlertDialog.isInitialized)
            progressAlertDialog.dismiss()
    }
}