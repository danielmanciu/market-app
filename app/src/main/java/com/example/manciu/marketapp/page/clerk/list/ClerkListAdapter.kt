package com.example.manciu.marketapp.page.clerk.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.manciu.marketapp.R
import com.example.manciu.marketapp.data.local.persistence.ProductEntity
import com.example.manciu.marketapp.utils.callback.ItemClickCallback
import com.example.manciu.marketapp.utils.callback.ItemPositionClickCallback
import kotlinx.android.synthetic.main.item_product_clerk.view.*
import timber.log.Timber

class ClerkListAdapter(
        private val deleteClickCallback: ItemPositionClickCallback,
        private val showDetailsClickCallback: ItemClickCallback
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
            holder.bind(position, showDetailsClickCallback, deleteClickCallback)

    override fun getItemCount() = products?.size ?: 0

    inner class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(position: Int,
                 showDetailsClickCallback: ItemClickCallback,
                 deleteClickCallback: ItemPositionClickCallback) {
            val product: ProductEntity = products!![position]
            val id: Int = product.id

            itemView.run {
                productNameTextView.text = product.name
                productQuantityTextView.text = "${product.quantity}"
                productPriceTextView.text = "$${product.price}"

                rootCardView.transitionName = "$id-rootCardView"
                productNameTextView.transitionName = "$id-name"
                quantityIcon.transitionName = "$id-quantityIcon"
                productQuantityTextView.transitionName = "$id-quantity"
                priceIcon.transitionName = "$id-priceIcon"
                productPriceTextView.transitionName = "$id-price"

                detailsClickableArea.setOnClickListener {
                    showDetailsClickCallback.onClick(product, itemView)
                }

                deleteButton.setOnClickListener {
                    deleteClickCallback.onClick(product, position)
                }
            }
        }
    }
}
