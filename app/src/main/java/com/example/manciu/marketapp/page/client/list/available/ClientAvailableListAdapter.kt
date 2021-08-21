package com.example.manciu.marketapp.page.client.list.available

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.manciu.marketapp.R
import com.example.manciu.marketapp.data.local.persistence.ProductEntity
import com.example.manciu.marketapp.utils.callback.ItemClickCallback
import com.example.manciu.marketapp.utils.callback.ItemPositionClickCallback
import kotlinx.android.synthetic.main.item_product_client.view.*

class ClientAvailableListAdapter(
    private val buyClickCallback: ItemPositionClickCallback,
    private val showDetailsClickCallback: ItemClickCallback
) : RecyclerView.Adapter<ClientAvailableListAdapter.ProductViewHolder>() {

    var products: MutableList<ProductEntity>? = null

    fun setProductList(list: List<ProductEntity>) {
        this.products = list.toMutableList()
        notifyDataSetChanged()
    }

    fun addProductAndNotify(product: ProductEntity) {
        products?.toMutableList().let {
            it?.add(product)
            notifyItemInserted(it?.size ?: 0)
            products = it
        }
    }

    override fun getItemId(position: Int): Long {
        return products!![position].id.toLong()
    }

    fun changeProductAndNotify(product: ProductEntity, position: Int) =
        if (product.quantity > 0) updateProductAndNotify(product, position)
        else deleteProductAndNotify(position)

    private fun updateProductAndNotify(product: ProductEntity, position: Int) =
        products?.run {
            this[position] = product
            notifyItemChanged(position)
        }

    private fun deleteProductAndNotify(position: Int) =
        products?.run {
            this.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, this.size)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product_client, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) =
        holder.bind(position, showDetailsClickCallback, buyClickCallback)

    override fun getItemCount() = products?.size ?: 0

    inner class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(
            position: Int,
            showDetailsClickCallback: ItemClickCallback,
            buyClickCallback: ItemPositionClickCallback
        ) {
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

                buyButton.setOnClickListener {
                    buyClickCallback.onClick(product, position)
                }
            }
        }
    }
}
