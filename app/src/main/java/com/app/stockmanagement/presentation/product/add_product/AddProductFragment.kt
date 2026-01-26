package com.app.stockmanagement.presentation.product.add_product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.app.stockmanagement.R
import com.app.stockmanagement.data.local.entity.Supplier
import com.app.stockmanagement.databinding.FragmentAddProductBinding
import com.app.stockmanagement.domain.model.Product
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddProductFragment : DialogFragment() {
    private var _binding: FragmentAddProductBinding? = null
    private val binding get() = _binding!!
    lateinit var suppliers: List<Supplier>
    private val viewModel: AddProductViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(
            STYLE_NORMAL,
            com.google.android.material.R.style.Theme_Material3_DayNight_NoActionBar
        )
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    when {
                        uiState.isSuccessful -> {
                            findNavController().popBackStack()

                        }

                        uiState.suppliers.isNotEmpty() -> {
                            suppliers = uiState.suppliers
                            val adapter = ArrayAdapter(
                                requireContext(),
                                android.R.layout.simple_list_item_1,
                                uiState.suppliers.map { it.name }
                            )
                            binding.productForm.supplierAutoComplete.setAdapter(adapter)

                        }
                    }
                }
            }


        }
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
        binding.productForm.categoryAutoComplete.setAdapter(adapter)
        binding.topAppBar.setOnMenuItemClickListener {
            val product = Product(
                name = binding.productForm.name.text.toString(),
                description = binding.productForm.description.text.toString(),
                price = binding.productForm.price.text.toString().toDouble(),
                category = binding.productForm.categoryAutoComplete.text.toString(),
                barcode = binding.productForm.barcode.text.toString(),
                supplierId = suppliers.first { it.name == binding.productForm.supplierAutoComplete.text.toString() }.id,
                currentStockLevel = binding.productForm.currentStockLevel.text.toString().toInt(),
                minimumStockLevel = binding.productForm.minStockLevel.text.toString().toInt()

            )
            viewModel.addNewProduct(product)
            return@setOnMenuItemClickListener true
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}