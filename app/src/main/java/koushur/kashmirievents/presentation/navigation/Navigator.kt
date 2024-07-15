package koushur.kashmirievents.presentation.navigation

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import koushur.kashmirievents.presentation.ui.main.ActivityMain
import koushur.kashmirievents.presentation.ui.main.addevent.AddEventFragment

/**
 * Navigation utility file
 */
object Navigator {
    fun navigateToAddEvent(activity: FragmentActivity, bundle: Bundle) {
        navigateAdd(
            context = activity,
            args = bundle,
            fragment = AddEventFragment(),
            container = android.R.id.content,
            addToBackStack = true
        )
    }

    fun restartMainActivity(appCompatActivity: FragmentActivity?) {
        val intent = Intent(appCompatActivity, ActivityMain::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        appCompatActivity?.finish()
        appCompatActivity?.startActivity(Intent(appCompatActivity, ActivityMain::class.java))
    }

    fun redirectToFragment(
        fragment: Fragment,
        context: FragmentActivity,
        args: Bundle? = null,
        container: Int,
        addToBackStack: Boolean = false
    ) {
        if (!context.supportFragmentManager.fragments.contains(fragment)) navigateReplace(
            context = context,
            fragment = fragment,
            args = args,
            container = container,
            addToBackStack = addToBackStack
        )
    }

    fun redirectToDialogFragment(
        context: FragmentActivity, fragment: DialogFragment, args: Bundle? = null
    ) {
        showDialogFragment(context = context, args = args, fragment = fragment)
    }

    private fun navigateAdd(
        context: Context,
        args: Bundle? = null,
        fragment: Fragment,
        container: Int,
        addToBackStack: Boolean = false
    ) {
        args?.let {
            fragment.arguments = it
        }
        TransitionManager.addFragment(
            context as AppCompatActivity, fragment, container, addToBackStack, true
        )
    }

    private fun navigateAllowingStateLose(
        context: Context,
        args: Bundle? = null,
        fragment: Fragment,
        container: Int,
        addToBackStack: Boolean = false
    ) {
        args?.let {
            fragment.arguments = args
        }
        TransitionManager.replaceFragmentAllowingStateLoss(
            context as AppCompatActivity,
            fragment,
            container,
            addToBackStack,
            TransitionManager.TransitionAnimation.TRANSITION_NONE
        )
    }

    private fun navigateReplace(
        context: Context,
        args: Bundle? = null,
        fragment: Fragment,
        container: Int,
        addToBackStack: Boolean = false
    ) {
        args?.let {
            fragment.arguments = args
        }
        TransitionManager.replaceFragment(
            context as AppCompatActivity,
            fragment,
            container,
            addToBackStack,
            TransitionManager.TransitionAnimation.TRANSITION_NONE
        )
    }

    private fun showDialogFragment(
        context: Context, args: Bundle? = null, fragment: AppCompatDialogFragment
    ) {
        args?.let {
            fragment.arguments = args
        }
        TransitionManager.openDialogFragment(
            context as AppCompatActivity, fragment
        )
    }

    private fun showDialogFragment(
        context: Context, args: Bundle? = null, fragment: DialogFragment
    ) {
        args?.let {
            fragment.arguments = args
        }
        TransitionManager.openDialogFragment(
            context as AppCompatActivity, fragment
        )
    }

    fun navigateToPreviousFrag(activity: Activity) {
        TransitionManager.popBackStackImmediate(activity as AppCompatActivity)
    }
}