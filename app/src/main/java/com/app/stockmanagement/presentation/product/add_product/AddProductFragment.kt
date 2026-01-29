package com.app.stockmanagement.presentation.product.add_product

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.app.stockmanagement.R
import com.app.stockmanagement.databinding.FragmentAddProductBinding
import com.app.stockmanagement.domain.model.Product
import com.app.stockmanagement.domain.model.Supplier
import com.app.stockmanagement.presentation.BaseCameraFragment
import com.app.stockmanagement.util.Constants.ERROR
import com.app.stockmanagement.util.UseCaseHandler
import com.google.android.material.snackbar.Snackbar
import com.google.mlkit.vision.barcode.common.Barcode
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddProductFragment : BaseCameraFragment() {
    private var _binding: FragmentAddProductBinding? = null
    private val binding get() = _binding!!
    lateinit var suppliers: List<Supplier>
    private val viewModel: AddProductViewModel by viewModels()
    private lateinit var formFields: List<EditText>
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
        binding.topAppBar.menu.findItem(R.id.action_save)?.isEnabled = false
        binding.btnCloseCamera.setOnClickListener {
            stopCamera()
        }

        binding.productForm.barcodeLayout.setEndIconOnClickListener {
            binding.cameraContainer.isVisible = true
            binding.previewView.controller = cameraController
            checkForPermissionAndScanBarCode(callback = object : UseCaseHandler<Barcode> {
                override fun onSuccess(result: Barcode) {
                    binding.productForm.barcode.setText(result.displayValue)
                    stopCamera()
                }

                override fun onFailure() {
                    Snackbar.make(
                        binding.root, ERROR, Snackbar.LENGTH_LONG
                    ).setBackgroundTint(Color.RED).show()
                }

            })

        }
        formFields = with(binding.productForm) {
            listOf(
                name, description, price, categoryAutoComplete,
                barcode, supplierAutoComplete, currentStockLevel, minStockLevel
            )
        }
        formFields.map { editText ->
            editText.doAfterTextChanged {
                if (editText == binding.productForm.currentStockLevel || editText == binding.productForm.minStockLevel) {
                    validateStockFields()
                }
                validateForm()
            }
        }

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

    private fun allRequiredFieldsPresent(): Boolean =
        with(binding.productForm) {
            listOf(
                name, description, price, categoryAutoComplete,
                barcode, supplierAutoComplete, currentStockLevel, minStockLevel
            ).all { it.text?.toString()?.trim()?.isNotBlank() == true }
        }


    private fun validateForm() {
        if (allRequiredFieldsPresent()) binding.topAppBar.menu.findItem(R.id.action_save)?.isEnabled =
            true

    }

    private fun validateStockFields() = with(binding.productForm) {
        val currentStockLevelValue = currentStockLevel.text.toString().toIntOrNull() ?: -1
        val minStockLevelValue = minStockLevel.text.toString().toIntOrNull() ?: -1
        if (currentStockLevelValue != -1 && currentStockLevelValue < minStockLevelValue) {
            currentStockLevelLayout.error = getString(R.string.can_t_be_lower_than_minimum_stock)
        } else {
            currentStockLevelLayout.error = null

        }
    }

    private fun stopCamera() {
        cameraController.unbind()
        binding.cameraContainer.isVisible = false
        binding.previewView.controller = null
    }
}


