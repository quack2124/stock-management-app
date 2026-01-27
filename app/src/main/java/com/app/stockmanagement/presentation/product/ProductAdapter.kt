package com.app.stockmanagement.presentation.product

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.app.stockmanagement.R
import com.app.stockmanagement.databinding.ItemProductBinding
import com.app.stockmanagement.domain.model.ProductWithSupplier

class ProductAdapter(private var products: List<ProductWithSupplier>) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {
    private var isEditEnabled: Boolean = true
    private val expandedPositions = mutableSetOf<Int>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun getItemCount(): Int = products.size

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        holder.bind(product, position)
    }

    inner class ProductViewHolder(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(product: ProductWithSupplier, position: Int) {
            binding.apply {
                productName.text = product.name
                productCategory.text = product.category
                productPrice.text = "$${product.price}"
                productStock.text = "Stock: ${product.currentStockLevel}"
                productDescription.text = "Description: ${product.description}"
                productBarcode.text = "Barcode: ${product.barcode}"
                productSupplier.text = "Supplier: ${product.supplierName}"
                val isExpanded = expandedPositions.contains(position)

                if (isExpanded) expandIcon.setImageResource(R.drawable.ic_arrow_up_24) else expandIcon.setImageResource(
                    R.drawable.ic_arrow_down_24
                )

                editIcon.visibility = if (isExpanded && isEditEnabled) View.VISIBLE else View.GONE
                productDescription.visibility = if (isExpanded) View.VISIBLE else View.GONE
                productBarcode.visibility = if (isExpanded) View.VISIBLE else View.GONE
                productSupplier.visibility = if (isExpanded) View.VISIBLE else View.GONE
                editIcon.setOnClickListener {
                    val action = ProductFragmentDirections.actionProductToEdit(product)
                    binding.root.findNavController().navigate(action)
                }

                binding.root.setOnClickListener {
                    if (isExpanded) {
                        expandedPositions.remove(position)
                    } else {
                        expandedPositions.add(position)
                    }
                    notifyItemChanged(position)
                }
            }
        }
    }

    fun updateData(newList: List<ProductWithSupplier>) {
        products = newList
        expandedPositions.clear()
        notifyDataSetChanged()
    }

    fun hideEditBtn() {
        this.isEditEnabled = false
        notifyDataSetChanged()
    }
}