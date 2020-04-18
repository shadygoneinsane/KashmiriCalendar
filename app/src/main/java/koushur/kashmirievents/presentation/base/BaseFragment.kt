package koushur.kashmirievents.presentation.base

import android.app.AlertDialog
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import koushur.kashmirievents.presentation.common.SingleEvent
import koushur.kashmirievents.presentation.common.extensions.observe
import koushur.kashmirievents.presentation.common.extensions.toast
import dagger.android.support.DaggerFragment
import koushir.kashmirievents.R
import javax.inject.Inject


abstract class BaseFragment<VB : ViewDataBinding, VM : BaseViewModel> : DaggerFragment() {

    abstract fun provideViewModelClass(): Class<VM>
    abstract fun layoutId(): Int
    abstract val viewModelVariable: Int

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    protected lateinit var viewBinding: VB
    protected lateinit var viewModel: VM
    private lateinit var progressAlertDialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory)[provideViewModelClass()]
        handleObserver()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(layoutId(), container, false)
        viewBinding = DataBindingUtil.bind(view)!!
        viewBinding.lifecycleOwner = this
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.setVariable(viewModelVariable, viewModel)
        viewBinding.executePendingBindings()
    }

    private fun handleObserver() {
        observe(viewModel.mErrorLiveData, ::observeError)
        observe(viewModel.mLoadingLiveData, ::observeLoading)
    }

    internal fun observeError(error: SingleEvent<String>) {
        // handle error here
        error.getContentIfNotHandled()?.let { err ->
            context?.let {
                err.toast(it)
            }
        }
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