package com.example.manciu.marketapp.page.clerk.add

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.manciu.marketapp.R
import com.example.manciu.marketapp.base.BaseFragment
import com.example.manciu.marketapp.data.persistence.ProductEntity
import com.example.manciu.marketapp.utils.showShortToast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_add.*
import timber.log.Timber

class AddFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        confirmButton.setOnClickListener {
            insertProduct()
        }
    }

    private fun getProductFromInputs() = ProductEntity(
            //this id will be replaced by the server
            id = -1,
            name = "${nameEditText.text}",
            description = "${descriptionEditText.text}",
            quantity = "${quantityEditText.text}".toInt(),
            price = "${priceEditText.text}".toInt(),
            status = "${statusEditText.text}"
    )

    private fun insertProduct() {
        if (areInputsEmpty()) {
            showShortToast(activity, "Inputs cannot be empty.")
            return
        }

        val productEntity: ProductEntity = getProductFromInputs()
        val liveData: MutableLiveData<ProductEntity> = MutableLiveData()

        val d: Disposable = viewModel.insertProductRemote(productEntity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    response.body()?.let {
                        liveData.value = it.convertRemoteToLocal()

                        showShortToast(activity, "Successfully added ${it.name}")
                    }
                },
                        { error -> Timber.e(error, "Unable to add product.") }
                )

        addDisposable(d)

        liveData.observe(this, Observer {
            navController.popBackStack()
        })
    }

    private fun areInputsEmpty(): Boolean {
        return nameEditText.text.isNullOrBlank()
                || descriptionEditText.text.isNullOrBlank()
                || quantityEditText.text.isNullOrBlank()
                || priceEditText.text.isNullOrBlank()
                || statusEditText.text.isNullOrBlank()
    }

}