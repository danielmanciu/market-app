package com.example.manciu.marketapp.page.client.details

import androidx.lifecycle.MutableLiveData
import com.example.manciu.marketapp.base.BaseViewModel
import com.example.manciu.marketapp.data.persistence.ProductEntity
import com.example.manciu.marketapp.data.remote.RemoteService
import com.example.manciu.marketapp.utils.Outcome
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class DetailsViewModel(private val service: RemoteService) : BaseViewModel() {

    val productLiveData: MutableLiveData<Outcome<ProductEntity>> = MutableLiveData()

    fun getProductRemote(id: Int) {
        productLiveData.value = Outcome.loading(true)

        val d: Disposable = service.getProduct(id)
            .subscribeOn(Schedulers.io())
            .filter { it.isSuccessful }
            .map { it.body() }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ list ->
                list?.let {
                    val product = it.convertRemoteToLocal()
                    productLiveData.value = Outcome.success(product)
                }
            },
                { error ->
                    Timber.e(error, "Unable to get available products list.")
                    productLiveData.value = Outcome.failure(error)
                }
            )

        addDisposable(d)
    }

}