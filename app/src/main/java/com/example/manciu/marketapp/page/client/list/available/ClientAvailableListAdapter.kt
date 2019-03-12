package com.example.manciu.marketapp.page.client.list.available

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.manciu.marketapp.R
import com.example.manciu.marketapp.data.persistence.ProductEntity
import com.example.manciu.marketapp.utils.callback.ItemClickCallback
import kotlinx.android.synthetic.main.item_product_client.view.*

class ClientAvailableListAdapter(
        private val buyClickCallback: ItemClickCallback
) : RecyclerView.Adapter<ClientAvailableListAdapter.ProductViewHolder>() {

    var products: List<ProductEntity>? = null

    fun setProductList(list: List<ProductEntity>) {
        this.products = list
        notifyDataSetChanged()
    }

    fun addProductAndNotify(product: ProductEntity) {
        products?.toMutableList().let {
            it?.add(product)
            notifyItemInserted(it?.size ?: 0)
            products = it
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_product_client, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) =
            holder.bind(position, buyClickCallback)

    override fun getItemCount() = products?.size ?: 0

    inner class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(position: Int, listener: ItemClickCallback) {
            val product: ProductEntity = products!![position]

            itemView.productTextView.text = formatProductItemDetails(product)

            itemView.buyButton.setOnClickListener { listener.onClick(product) }
        }

        private fun formatProductItemDetails(product: ProductEntity): String =
                product.run {
                    "$name (x$quantity) - $$price"
                }

    }

}