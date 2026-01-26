package com.app.stockmanagement.presentation.supplier

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.app.stockmanagement.R
import com.app.stockmanagement.databinding.ItemSupplierBinding
import com.app.stockmanagement.domain.model.Supplier

class SupplierAdapter(private var suppliers: List<Supplier>) :
    RecyclerView.Adapter<SupplierAdapter.SupplierViewHolder>() {
    private val expandedPositions = mutableSetOf<Int>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SupplierViewHolder {
        val binding =
            ItemSupplierBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SupplierViewHolder(binding)
    }

    override fun getItemCount(): Int = suppliers.size

    override fun onBindViewHolder(holder: SupplierViewHolder, position: Int) {
        val supplier = suppliers[position]
        holder.bind(supplier, position)
    }

    inner class SupplierViewHolder(private val binding: ItemSupplierBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(supplier: Supplier, position: Int) {
            binding.apply {
                val isExpanded = expandedPositions.contains(position)

                if (isExpanded) expandIcon.setImageResource(R.drawable.ic_arrow_up_24) else expandIcon.setImageResource(
                    R.drawable.ic_arrow_down_24
                )

                editIcon.visibility = if (isExpanded) View.VISIBLE else View.GONE
                phone.visibility = if (isExpanded) View.VISIBLE else View.GONE
                email.visibility = if (isExpanded) View.VISIBLE else View.GONE
                address.visibility = if (isExpanded) View.VISIBLE else View.GONE
                editIcon.setOnClickListener {
                    val action = SupplierFragmentDirections.actionSupplierToEdit(supplier)
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

                name.text = supplier.name
                contactPerson.text = supplier.contactPerson
                phone.text = supplier.phone
                email.text = supplier.email
                address.text = supplier.address
            }
        }
    }

    fun updateData(newList: List<Supplier>) {
        this.suppliers = newList
        notifyDataSetChanged()
    }

}