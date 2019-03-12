package com.example.manciu.marketapp.page.client.list.bought

import androidx.lifecycle.MutableLiveData
import com.example.manciu.marketapp.base.BaseViewModel
import com.example.manciu.marketapp.data.persistence.ProductEntity
import com.example.manciu.marketapp.data.persistence.ProductRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class ClientBoughtListViewModel(private val repository: ProductRepository) : BaseViewModel() {

    val boughtProductsLiveData: MutableLiveData<List<ProductEntity>> = MutableLiveData()

    fun getBoughtProductsLocal() {
        val d: Disposable = repository.getBoughtProducts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    boughtProductsLiveData.value = it
                },
                        { error ->
                            Timber.e(error, "Unable to get product list.")
                        }
                )

        addDisposable(d)
    }
}