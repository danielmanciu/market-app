package com.example.manciu.marketapp.page.client.list.available

import androidx.lifecycle.MutableLiveData
import com.example.manciu.marketapp.base.BaseViewModel
import com.example.manciu.marketapp.data.local.persistence.ProductEntity
import com.example.manciu.marketapp.data.local.persistence.ProductRepository
import com.example.manciu.marketapp.data.remote.ProductRemoteEntity
import com.example.manciu.marketapp.data.remote.RemoteService
import com.example.manciu.marketapp.utils.Outcome
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

    val availableListLiveData: MutableLiveData<Outcome<List<ProductEntity>>> = MutableLiveData()

    fun getAvailableProductsRemote() {
        availableListLiveData.value = Outcome.loading(true)

        val d: Disposable = service.getAvailableProducts()
                .subscribeOn(Schedulers.io())
                .filter { it.isSuccessful }
                .map { it.body() }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ remoteList ->
                    remoteList?.let {
                        val list = it.stream()
                                .map(ProductRemoteEntity::convertRemoteToLocal)
                                .collect(Collectors.toList())

                        availableListLiveData.value = Outcome.success(list)
                    }
                },
                        { error ->
                            Timber.e(error, "Unable to get available products list.")
                            availableListLiveData.value = Outcome.failure(error)
                        }
                )

        addDisposable(d)
    }

    fun buyProduct(product: ProductEntity, callback: (ProductEntity) -> Unit) {
        val d: Disposable = service.buyProduct(product.convertLocalToRemote())
                .subscribeOn(Schedulers.io())
                .filter { it.isSuccessful }
                .map { it.body() }
                .flatMap {
                    repository.insertProduct(product)

                    Observable.just(it.convertRemoteToLocal())
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