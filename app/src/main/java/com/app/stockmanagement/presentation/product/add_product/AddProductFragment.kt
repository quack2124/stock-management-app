package com.app.stockmanagement.presentation.product.add_product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.app.stockmanagement.R
import com.app.stockmanagement.databinding.FragmentAddProductBinding
import com.app.stockmanagement.domain.model.Product
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddProductFragment : DialogFragment() {
    private var _binding: FragmentAddProductBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AddProductViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(
            STYLE_NORMAL,
            com.google.android.material.R.style.Theme_Material3_DayNight_NoActionBar
        )

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddProductBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.topAppBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        val categories = resources.getStringArray(R.array.categories)
        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, categories)
        binding.categoryAutoComplete.setAdapter(adapter)
        binding.topAppBar.setOnMenuItemClickListener {
            val product = Product(
                name = binding.name.text.toString(),
                description = binding.description.text.toString(),
                price = binding.price.text.toString().toDouble(),
                category = binding.categoryAutoComplete.text.toString(),
                barcode = binding.barcode.text.toString(),
                supplierId = 1, // will be replace with appropriate id
                currentStockLevel = binding.currentStockLevel.text.toString().toInt(),
                minimumStockLevel = binding.minStockLevel.text.toString().toInt()

            )
            viewModel.addNewProduct(product)
            return@setOnMenuItemClickListener true;
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}