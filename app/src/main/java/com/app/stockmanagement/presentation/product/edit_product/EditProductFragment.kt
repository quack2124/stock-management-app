package com.app.stockmanagement.presentation.product.edit_product

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.app.stockmanagement.databinding.FragmentEditProductBinding
import com.app.stockmanagement.domain.model.ProductWithSupplier
import com.app.stockmanagement.presentation.BaseCameraFragment
import com.app.stockmanagement.util.Constants.ERROR
import com.app.stockmanagement.util.UseCaseHandler
import com.google.android.material.snackbar.Snackbar
import com.google.mlkit.vision.barcode.common.Barcode
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EditProductFragment : BaseCameraFragment() {
    private var _binding: FragmentEditProductBinding? = null
    private val binding get() = _binding!!
    private val viewModel: EditProductViewModel by viewModels()
    private val args: EditProductFragmentArgs by navArgs()
    private lateinit var product: ProductWithSupplier
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(
            STYLE_NORMAL, com.google.android.material.R.style.Theme_Material3_DayNight_NoActionBar
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        product = args.currentProduct

        binding.btnCloseCamera.setOnClickListener { stopCamera() }

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

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.eventFlow.collect { event ->
                    when (event) {
                        is EditProductEvent.Success -> findNavController().popBackStack()

                        is EditProductEvent.ShowError -> Snackbar.make(
                            binding.root, event.message, Snackbar.LENGTH_LONG
                        ).setBackgroundTint(Color.RED).show()

                    }
                }
            }
        }
        with(binding.productForm) {
            name.setText(product.name)
            description.setText(product.description)
            price.setText(product.price.toString())
            categoryAutoComplete.isEnabled = false
            categoryAutoComplete.setText(product.category)
            barcode.setText(product.barcode)
            supplierAutoComplete.isEnabled = false
            supplierAutoComplete.setText(product.supplierName)
            currentStockLevel.setText(product.currentStockLevel.toString())
            minStockLevel.setText(product.minimumStockLevel.toString())
        }
        binding.topAppBar.setOnMenuItemClickListener {
            product = product.copy(
                name = binding.productForm.name.text.toString(),
                description = binding.productForm.description.text.toString(),
                price = binding.productForm.price.text.toString().toDouble(),
                barcode = binding.productForm.barcode.text.toString(),
                currentStockLevel = binding.productForm.currentStockLevel.text.toString().toInt(),
                minimumStockLevel = binding.productForm.minStockLevel.text.toString().toInt(),
            )
            viewModel.updateProduct(product)
            return@setOnMenuItemClickListener true
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditProductBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.topAppBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        return root
    }

    private fun stopCamera() {
        cameraController.unbind()
        binding.cameraContainer.isVisible = false
        binding.previewView.controller = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}