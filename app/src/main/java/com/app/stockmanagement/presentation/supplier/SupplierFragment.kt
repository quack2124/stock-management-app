package com.app.stockmanagement.presentation.supplier

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.stockmanagement.databinding.FragmentSupplierBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SupplierFragment : Fragment() {

    private var _binding: FragmentSupplierBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SupplierViewModel by viewModels()
    private var supplierAdapter = SupplierAdapter(emptyList())
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.uiState.collect { uiState ->
                        binding.progressBar.isVisible = uiState.isLoading
                        supplierAdapter.updateData(uiState.suppliers)
                    }
                }

                launch {
                    viewModel.eventFlow.collect { event ->
                        if (event is SupplierUiEffect.ShowError) {
                            Snackbar.make(
                                binding.root, event.message, Snackbar.LENGTH_LONG
                            ).setBackgroundTint(Color.RED).show()
                        }
                    }
                }
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSupplierBinding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.supplierList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = supplierAdapter
        }


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}