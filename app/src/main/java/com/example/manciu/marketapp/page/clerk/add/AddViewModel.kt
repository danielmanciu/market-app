package com.example.manciu.marketapp.page.clerk.add

import androidx.lifecycle.MutableLiveData
import com.example.manciu.marketapp.base.BaseViewModel
import com.example.manciu.marketapp.data.persistence.ProductEntity
import com.example.manciu.marketapp.data.remote.RemoteService
import com.example.manciu.marketapp.utils.Outcome
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class AddViewModel(private val service: RemoteService) : BaseViewModel() {

    val addProductLiveData: MutableLiveData<Outcome<ProductEntity>> = MutableLiveData()

    fun insertProductRemote(product: ProductEntity) {
        addProductLiveData.value = Outcome.loading(true)

        val d: Disposable = service.insertProduct(product.convertLocalToRemote())
                .subscribeOn(Schedulers.io())
                .filter { it.isSuccessful }
                .map { it.body() }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ remoteProduct ->
                    remoteProduct?.let {
                        val localProduct = it.convertRemoteToLocal()
                        addProductLiveData.value = Outcome.success(localProduct)
                    }
                },
                        { error ->
                            Timber.e(error, "Unable to add product.")
                            addProductLiveData.value = Outcome.failure(error)
                        }
                )

        addDisposable(d)
    }
}