package com.example.manciu.marketapp.page.clerk.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.manciu.marketapp.R
import com.example.manciu.marketapp.data.persistence.ProductEntity
import com.example.manciu.marketapp.utils.callback.ItemClickCallback
import kotlinx.android.synthetic.main.item_product_clerk.view.*

class ClerkListAdapter(
        private val deleteClickCallback: ItemClickCallback
) : RecyclerView.Adapter<ClerkListAdapter.ProductViewHolder>() {

    var products: List<ProductEntity>? = null

    fun setProductList(list: List<ProductEntity>) {
        this.products = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_product_clerk, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) =
            holder.bind(position, deleteClickCallback)

    override fun getItemCount() = products?.size ?: 0

    inner class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(position: Int, listener: ItemClickCallback) {
            val product: ProductEntity = products!![position]

            itemView.productTextView.text = formatProductItemDetails(product)

            itemView.deleteButton.setOnClickListener { listener.onClick(product) }
        }

        private fun formatProductItemDetails(product: ProductEntity): String =
                product.run {
                    "$name (x$quantity) - $price"
                }

    }

}