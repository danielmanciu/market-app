package com.example.manciu.marketapp.page.client.list.available

import androidx.lifecycle.MutableLiveData
import com.example.manciu.marketapp.base.BaseViewModel
import com.example.manciu.marketapp.data.persistence.ProductEntity
import com.example.manciu.marketapp.data.persistence.ProductRepository
import com.example.manciu.marketapp.data.remote.ProductRemoteEntity
import com.example.manciu.marketapp.data.remote.RemoteService
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.util.stream.Collectors

class ClientAvailableListViewModel(
        private val service: RemoteService,
        private val repository: ProductRepository
) : BaseViewModel() {

    val availableListLiveData: MutableLiveData<List<ProductEntity>> = MutableLiveData()

    fun getAvailableProductsRemote() {
        val d: Disposable = service.getAvailableProducts()
                .subscribeOn(Schedulers.io())
                .filter { it.isSuccessful }
                .map { it.body() }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ list ->
                    list?.let {
                        availableListLiveData.value = it.stream()
                                .map(ProductRemoteEntity::convertRemoteToLocal)
                                .collect(Collectors.toList())
                    }
                },
                        { error ->
                            Timber.e(error, "Unable to get available products list.")
                        }
                )

        addDisposable(d)
    }

    fun buyProduct(product: ProductEntity, callback: (ProductEntity) -> Unit) {
        val d: Disposable = service.buyProduct(product.convertLocalToRemote())
                .subscribeOn(Schedulers.io())
                .filter { it.isSuccessful }
                .flatMap {
                    repository.insertProduct(product)

                    Observable.just(product)
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ boughtProduct ->
                    boughtProduct?.let { callback(it) }
                },
                        { error ->
                            Timber.e(error, "Unable to process product purchase.")
                        }
                )

        addDisposable(d)
    }
}