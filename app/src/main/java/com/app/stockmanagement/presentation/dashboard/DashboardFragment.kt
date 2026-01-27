package com.app.stockmanagement.presentation.dashboard

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
import com.app.stockmanagement.databinding.FragmentDashboardBinding
import com.app.stockmanagement.presentation.product.ProductAdapter
import com.app.stockmanagement.presentation.transaction.TransactionAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.Tab
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DashboardViewModel by viewModels()
    private var transactionAdapter = TransactionAdapter(emptyList())
    private var productsAdapter = ProductAdapter(emptyList())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root
        viewModel.getTransactionsInLast24Hrs()
        viewModel.getLowStockProducts()
        binding.recentTransactionList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = transactionAdapter
        }

        productsAdapter.hideEditBtn()
        binding.lowStockList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = productsAdapter
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    transactionAdapter.updateData(uiState.transactions)
                    productsAdapter.updateData(uiState.products)
                    binding.progressBarDashboard.isVisible = uiState.isLoading
                }
            }
        }


        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: Tab) {
                when (tab.position) {
                    0 -> {
                        binding.lowStockList.isVisible = false
                        binding.recentTransactionList.isVisible = true
                    }

                    1 -> {
                        binding.lowStockList.isVisible = true
                        binding.recentTransactionList.isVisible = false
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}