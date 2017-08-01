package com.popalay.cardme.presentation.base.navigation

import android.content.Intent
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import com.popalay.cardme.R
import com.popalay.cardme.presentation.base.BaseActivity
import com.popalay.cardme.presentation.base.navigation.commands.ForwardForResult
import com.popalay.cardme.presentation.base.navigation.commands.ForwardToUrl
import com.popalay.cardme.utils.BrowserUtils
import com.popalay.cardme.utils.extensions.currentFragment
import ru.terrakok.cicerone.android.SupportAppNavigator
import ru.terrakok.cicerone.commands.Command

open class CustomNavigator(
        val activity: BaseActivity,
        containerId: Int = 0
) : SupportAppNavigator(activity, containerId) {

    val fragmentManager: FragmentManager = activity.supportFragmentManager

    override fun applyCommand(command: Command?) {
        if (command is ForwardToUrl) {
            BrowserUtils.openLink(activity, command.url)
        } else if (command is ForwardForResult) {
            val activityIntent = createActivityIntent(command.screenKey, command.transitionData)

            // Start activity
            if (activityIntent != null) {
                val currentFragment = fragmentManager.currentFragment()
                if (currentFragment != null) {
                    currentFragment.startActivityForResult(activityIntent, command.requestCode)
                } else {
                    activity.startActivityForResult(activityIntent, command.requestCode)
                }
            }
        }

        super.applyCommand(command)
    }

    override fun createFragment(screenKey: String, data: Any?): Fragment? = null

    override fun createActivityIntent(screenKey: String, data: Any?): Intent? = null

    override fun setupFragmentTransactionAnimation(command: Command?,
                                                   currentFragment: Fragment?,
                                                   nextFragment: Fragment?,
                                                   fragmentTransaction: FragmentTransaction?) {
        fragmentTransaction?.setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
    }
}