package com.popalay.cardme.presentation.screens.holders

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.popalay.cardme.R
import com.popalay.cardme.databinding.FragmentHoldersBinding
import com.popalay.cardme.presentation.base.BaseFragment
import com.popalay.cardme.presentation.base.navigation.NavigationExtrasHolder
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class HoldersFragment : BaseFragment() {

    @Inject lateinit var factory: ViewModelProvider.Factory
    @Inject lateinit var navigationExtras: NavigationExtrasHolder

    companion object {
        fun newInstance() = HoldersFragment()
    }

    private lateinit var b: FragmentHoldersBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        b = DataBindingUtil.inflate<FragmentHoldersBinding>(inflater, R.layout.fragment_holders, container, false)
        ViewModelProviders.of(this, factory).get(HoldersViewModel::class.java).let {
            b.vm = it
        }
        return b.root
    }
}
