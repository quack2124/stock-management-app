package com.app.stockmanagement.presentation.transaction.transaction_management

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.app.stockmanagement.data.local.entity.Type
import com.app.stockmanagement.databinding.FragmentTransactionManagementBinding
import com.app.stockmanagement.domain.model.ProductWithSupplier
import com.app.stockmanagement.domain.model.TransactionWithProduct
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Date

@AndroidEntryPoint
class TransactionManagementFragment : DialogFragment() {
    private val viewModel: TransactionManagementViewModel by viewModels()
    private var _binding: FragmentTransactionManagementBinding? = null
    private val binding get() = _binding!!
    private lateinit var selectedProduct: ProductWithSupplier
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(
            STYLE_NORMAL, com.google.android.material.R.style.Theme_Material3_DayNight_NoActionBar
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.uiState.collect { uiState ->
                        binding.progressBarTransactionNew.isVisible = uiState.isLoading
                        val adapter = ArrayAdapter(
                            requireContext(),
                            android.R.layout.simple_list_item_1,
                            uiState.products
                        )
                        binding.productAutoComplete.setAdapter(adapter)
                    }
                }

                launch {
                    viewModel.eventFlow.collect { event ->
                        when (event) {
                            is TransactionManagementUiEffect.Success -> findNavController().popBackStack()
                            is TransactionManagementUiEffect.ShowError -> Snackbar.make(
                                binding.root,
                                event.message,
                                Snackbar.LENGTH_LONG
                            )
                                .setBackgroundTint(Color.RED)
                                .show()
                        }
                    }
                }

            }


        }
        binding.topAppBar.setOnMenuItemClickListener {
            viewModel.addTransaction(
                TransactionWithProduct(
                    0,
                    date = Date(),
                    type = Type.valueOf(binding.typeAutocomplete.text.toString()),
                    quantity = binding.transactionQuantity.text.toString().toInt(),
                    notes = binding.notes.text.toString(),
                    productId = selectedProduct.id
                ), selectedProduct
            )
            return@setOnMenuItemClickListener true
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTransactionManagementBinding.inflate(inflater, container, false)
        val adapter = ArrayAdapter(
            requireContext(), android.R.layout.simple_list_item_1,
            Type.entries.toTypedArray()
        )
        binding.typeAutocomplete.setAdapter(adapter)
        binding.topAppBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.productAutoComplete.setOnItemClickListener { parent, _, position, _ ->
            selectedProduct = parent.getItemAtPosition(position) as ProductWithSupplier
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}