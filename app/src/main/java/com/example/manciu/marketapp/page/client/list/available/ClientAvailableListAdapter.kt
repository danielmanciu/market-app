package com.example.manciu.marketapp.page.client.list.available

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.manciu.marketapp.data.local.persistence.ProductEntity
import com.example.manciu.marketapp.databinding.ItemProductClientBinding
import com.example.manciu.marketapp.utils.callback.ItemClickCallback
import com.example.manciu.marketapp.utils.callback.ItemPositionClickCallback

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
        return products?.get(position)?.id?.toLong() ?: 0
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
        products?.let {
            it.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, it.size)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemProductClientBinding.inflate(inflater, parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) =
        holder.bind(position, showDetailsClickCallback, buyClickCallback)

    override fun getItemCount() = products?.size ?: 0

    inner class ProductViewHolder(private val binding: ItemProductClientBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(
            position: Int,
            showDetailsClickCallback: ItemClickCallback,
            buyClickCallback: ItemPositionClickCallback
        ) {
            val product: ProductEntity = products?.get(position) ?: return
            val id: Int = product.id

            binding.productNameTextView.text = product.name
            binding.productQuantityTextView.text = "${product.quantity}"
            binding.productPriceTextView.text = "$${product.price}"

            binding.rootCardView.transitionName = "$id-rootCardView"
            binding.productNameTextView.transitionName = "$id-name"
            binding.quantityIcon.transitionName = "$id-quantityIcon"
            binding.productQuantityTextView.transitionName = "$id-quantity"
            binding.priceIcon.transitionName = "$id-priceIcon"
            binding.productPriceTextView.transitionName = "$id-price"

            binding.detailsClickableArea.setOnClickListener {
                showDetailsClickCallback.onClick(product, binding)
            }

            binding.buyButton.setOnClickListener {
                buyClickCallback.onClick(product, position)
            }
        }
    }
}
