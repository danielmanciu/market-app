package com.example.manciu.marketapp.page.client.list.bought

import androidx.lifecycle.MutableLiveData
import com.example.manciu.marketapp.base.BaseViewModel
import com.example.manciu.marketapp.data.persistence.ProductEntity
import com.example.manciu.marketapp.data.persistence.ProductRepository
import com.example.manciu.marketapp.utils.Outcome
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class ClientBoughtListViewModel(private val repository: ProductRepository) : BaseViewModel() {

    val boughtProductsLiveData: MutableLiveData<Outcome<List<ProductEntity>>> = MutableLiveData()

    fun getBoughtProductsLocal() {
        boughtProductsLiveData.value = Outcome.loading(true)

        val d: Disposable = repository.getBoughtProducts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    boughtProductsLiveData.value = Outcome.success(it)
                },
                        { error ->
                            Timber.e(error, "Unable to get product list.")
                            boughtProductsLiveData.value = Outcome.Failure(error)
                        }
                )

        addDisposable(d)
    }
}