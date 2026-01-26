package com.app.stockmanagement.presentation.transaction

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.stockmanagement.R
import com.app.stockmanagement.databinding.ItemTransactionBinding
import com.app.stockmanagement.domain.model.TransactionWithProduct
import java.text.SimpleDateFormat
import java.util.Locale

class TransactionAdapter(private var transactions: List<TransactionWithProduct>) :
    RecyclerView.Adapter<TransactionAdapter.SupplierViewHolder>() {
    private val expandedPositions = mutableSetOf<Int>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SupplierViewHolder {
        val binding =
            ItemTransactionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SupplierViewHolder(binding)
    }

    override fun getItemCount(): Int = transactions.size

    override fun onBindViewHolder(holder: SupplierViewHolder, position: Int) {
        val transaction = transactions[position]
        holder.bind(transaction, position)
    }

    inner class SupplierViewHolder(private val binding: ItemTransactionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(transaction: TransactionWithProduct, position: Int) {
            binding.apply {
                val isExpanded = expandedPositions.contains(position)

                if (isExpanded) expandIcon.setImageResource(R.drawable.ic_arrow_up_24) else expandIcon.setImageResource(
                    R.drawable.ic_arrow_down_24
                )
                product.text = transaction.productName
                date.text =
                    SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(transaction.date)
                pricePerItem.text = "PPU $${transaction.productPrice.toInt()}"
                quantity.text = "QTY ${transaction.quantity}"
                type.text = "TYPE: ${transaction.type}"
                productBarcode.text = "Barcode: ${transaction.productBarcode}"
                note.text = "Note: ${transaction.notes}"

                type.visibility = if (isExpanded) View.VISIBLE else View.GONE
                productBarcode.visibility = if (isExpanded) View.VISIBLE else View.GONE
                note.visibility = if (isExpanded) View.VISIBLE else View.GONE
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

    fun updateData(newList: List<TransactionWithProduct>) {
        this.transactions = newList
        notifyDataSetChanged()
    }

}