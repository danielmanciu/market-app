package com.example.manciu.marketapp.page.client.list.bought

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.manciu.marketapp.data.local.persistence.ProductEntity
import com.example.manciu.marketapp.databinding.ItemProductClientBinding

class ClientBoughtListAdapter :
    RecyclerView.Adapter<ClientBoughtListAdapter.ProductViewHolder>() {

    var products: List<ProductEntity>? = null

    fun setProductList(list: List<ProductEntity>) {
        this.products = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemProductClientBinding.inflate(inflater, parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) =
        holder.bind(position)

    override fun getItemCount() = products?.size ?: 0

    inner class ProductViewHolder(private val binding: ItemProductClientBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(position: Int) {
            val product = products?.get(position) ?: return

            binding.productNameTextView.text = product.name
            binding.productQuantityTextView.text = product.quantity.toString()
            binding.productPriceTextView.text = "$${product.price}"
            binding.buyButton.isVisible = false
        }
    }
}
