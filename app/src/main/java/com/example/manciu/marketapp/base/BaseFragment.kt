package com.example.manciu.marketapp.base

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import dagger.android.support.DaggerFragment
import javax.inject.Inject

abstract class BaseFragment<VM : BaseViewModel, VMF : ViewModelProvider.Factory> : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: VMF

    lateinit var viewModel: VM

    lateinit var navController: NavController

    override fun onAttach(context: Context) {
        super.onAttach(context)

        viewModel = ViewModelProvider(this, viewModelFactory).get(getViewModelClass())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController(view)
    }

    protected abstract fun getViewModelClass(): Class<VM>
}
