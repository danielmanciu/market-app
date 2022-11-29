package com.example.manciu.marketapp.page.clerk.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.manciu.marketapp.data.local.persistence.ProductEntity
import com.example.manciu.marketapp.databinding.ItemProductClerkBinding
import com.example.manciu.marketapp.utils.callback.ItemClickCallback
import com.example.manciu.marketapp.utils.callback.ItemPositionClickCallback

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
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemProductClerkBinding.inflate(inflater, parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) =
        holder.bind(position, showDetailsClickCallback, deleteClickCallback)

    override fun getItemCount() = products?.size ?: 0

    inner class ProductViewHolder(private val binding: ItemProductClerkBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            position: Int,
            showDetailsClickCallback: ItemClickCallback,
            deleteClickCallback: ItemPositionClickCallback
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

            binding.deleteButton.setOnClickListener {
                deleteClickCallback.onClick(product, position)
            }
        }
    }
}
