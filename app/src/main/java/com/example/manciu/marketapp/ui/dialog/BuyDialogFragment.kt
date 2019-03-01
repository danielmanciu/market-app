package com.example.manciu.marketapp.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.example.manciu.marketapp.R
import com.example.manciu.marketapp.persistence.ProductEntity
import com.example.manciu.marketapp.ui.callback.DialogListener
import kotlinx.android.synthetic.main.dialog_buy.*
import java.lang.Integer.parseInt

class BuyDialogFragment : DialogFragment() {

    companion object {
        private const val BUY_DIALOG = "buy_dialog"
        private lateinit var dialogListener: DialogListener
        private lateinit var productEntity: ProductEntity

        fun createBuyDialogFragment(
            fragmentManager: FragmentManager,
            listener: DialogListener,
            productEntity: ProductEntity
        ): BuyDialogFragment {
            dialogListener = listener
            Companion.productEntity = productEntity

            return BuyDialogFragment().apply {
                show(fragmentManager, BUY_DIALOG)
            }
        }
    }

    private lateinit var buyDialog: Dialog

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        context?.let {
            buyDialog = Dialog(it)
            buyDialog.setContentView(View.inflate(it, R.layout.dialog_buy, null))
        }

        return buyDialog
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        buyDialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        return inflater.inflate(R.layout.dialog_buy, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        confirmBuyButton.setOnClickListener {
            if (!quantityEditText.text.isNullOrBlank()) {
                val newQuantity = parseInt(quantityEditText.text.toString())

                dialogListener.buyProduct(productEntity, newQuantity)
            }
        }
    }

}