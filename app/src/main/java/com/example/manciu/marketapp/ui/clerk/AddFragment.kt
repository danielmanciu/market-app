package com.example.manciu.marketapp.ui.clerk

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.manciu.marketapp.Injection
import com.example.manciu.marketapp.R
import com.example.manciu.marketapp.persistence.ProductEntity
import com.example.manciu.marketapp.ui.viewmodel.ProductViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_add.*

class AddFragment : Fragment() {

    companion object {
        val TAG: String? = AddFragment::class.qualifiedName
    }

    private lateinit var viewModel: ProductViewModel

    private val disposable: CompositeDisposable = CompositeDisposable()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = Injection.provideViewModel(activity!!)

        confirmButton.setOnClickListener {
            insertProduct()
        }
    }

    override fun onStop() {
        super.onStop()

        disposable.clear()
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
            Toast.makeText(activity, "Inputs cannot be empty.", Toast.LENGTH_SHORT).show()
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
                        Toast.makeText(activity, "Successfully added ${it.name}", Toast.LENGTH_SHORT).show()
                    }
                },
                        { error -> Log.e(TAG, "Unable to add product.", error) }
                )

        disposable.add(d)

        liveData.observe(this, Observer {
            fragmentManager?.popBackStack()
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