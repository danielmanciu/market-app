package com.example.manciu.marketapp.base

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import dagger.android.support.DaggerFragment
import javax.inject.Inject

abstract class BaseFragment<VM : BaseViewModel, VMF : ViewModelProvider.Factory> : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: VMF

    lateinit var viewModel: VM

    lateinit var navController: NavController

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(getViewModelClass())
        navController = findNavController()
    }

    protected abstract fun getViewModelClass(): Class<VM>
}