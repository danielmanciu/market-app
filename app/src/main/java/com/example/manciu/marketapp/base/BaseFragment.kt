package com.example.manciu.marketapp.base

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.manciu.marketapp.data.viewmodel.ProductViewModel
import com.example.manciu.marketapp.data.viewmodel.ProductViewModelFactory
import dagger.android.support.DaggerFragment
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import javax.inject.Inject

abstract class BaseFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ProductViewModelFactory

    protected lateinit var viewModel: ProductViewModel

    private var compositeDisposable: CompositeDisposable = CompositeDisposable()

    lateinit var navController: NavController

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ProductViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        navController = NavHostFragment.findNavController(this)
    }
    protected fun addDisposable(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }
}