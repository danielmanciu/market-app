package com.example.manciu.marketapp.page.client.list.bought

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.manciu.marketapp.R
import com.example.manciu.marketapp.data.local.persistence.ProductEntity
import kotlinx.android.synthetic.main.item_product_client.view.*

class ClientBoughtListAdapter :
        RecyclerView.Adapter<ClientBoughtListAdapter.ProductViewHolder>() {

    var products: List<ProductEntity>? = null

    fun setProductList(list: List<ProductEntity>) {
        this.products = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_product_client, parent, false)
        view.buyButton.visibility = View.GONE

        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) =
            holder.bind(position)

    override fun getItemCount() = products?.size ?: 0

    inner class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(position: Int) {
            val product: ProductEntity = products!![position]

            itemView.run {
                productNameTextView.text = product.name
                productQuantityTextView.text = "${product.quantity}"
                productPriceTextView.text = "$${product.price}"
            }
        }
    }
}