package com.example.manciu.marketapp.page.clerk.list

import androidx.lifecycle.MutableLiveData
import com.example.manciu.marketapp.base.BaseViewModel
import com.example.manciu.marketapp.data.persistence.ProductEntity
import com.example.manciu.marketapp.data.remote.ProductRemoteEntity
import com.example.manciu.marketapp.data.remote.RemoteService
import com.example.manciu.marketapp.utils.Outcome
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.util.stream.Collectors

class ClerkListViewModel(private val service: RemoteService) : BaseViewModel() {

    val clerkListLiveData: MutableLiveData<Outcome<List<ProductEntity>>> = MutableLiveData()

    fun getAllProductsRemote() {
        clerkListLiveData.value = Outcome.loading(true)

        val d: Disposable = service.getAllProducts()
                .subscribeOn(Schedulers.io())
                .filter { it.isSuccessful }
                .map { it.body() }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { remoteList ->
                            remoteList?.let {
                                val list = it.stream()
                                        .map(ProductRemoteEntity::convertRemoteToLocal)
                                        .collect(Collectors.toList())
                                clerkListLiveData.value = Outcome.success(list)
                            }
                        },
                        { error ->
                            Timber.e(error, "Unable to get product list.")
                            clerkListLiveData.value = Outcome.failure(error)}
                )

        addDisposable(d)
    }

    fun deleteProductRemote(product: ProductEntity, callback: () -> Unit) {
        val d: Disposable = service.deleteProduct(product.id)
                .subscribeOn(Schedulers.io())
                .filter { it.isSuccessful }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    callback()
                }, { error -> Timber.e(error, "Unable to delete product.") })

        addDisposable(d)
    }

}