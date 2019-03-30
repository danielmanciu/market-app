package com.example.manciu.marketapp.page.clerk.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.manciu.marketapp.R
import com.example.manciu.marketapp.data.persistence.ProductEntity
import com.example.manciu.marketapp.utils.callback.ItemPositionClickCallback
import kotlinx.android.synthetic.main.item_product_clerk.view.*

class ClerkListAdapter(
        private val deleteClickCallback: ItemPositionClickCallback
) : RecyclerView.Adapter<ClerkListAdapter.ProductViewHolder>() {

    var products: MutableList<ProductEntity>? = null

    fun setProductList(list: List<ProductEntity>) {
        this.products = list.toMutableList()
        notifyDataSetChanged()
    }

    fun deleteProductAndNotify(position: Int) =
        products?.run {
            this.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, this.size)
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

        fun bind(position: Int, listener: ItemPositionClickCallback) {
            val product: ProductEntity = products!![position]

            itemView.run {
                productNameTextView.text = product.name
                productQuantityTextView.text = "${product.quantity}"
                productPriceTextView.text = "$${product.price}"

                deleteButton.setOnClickListener {
                    listener.onClick(product, position)
                }
            }

        }

    }

}