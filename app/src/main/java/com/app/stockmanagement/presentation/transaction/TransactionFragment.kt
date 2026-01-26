package com.app.stockmanagement.presentation.transaction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.stockmanagement.R
import com.app.stockmanagement.data.local.entity.Type
import com.app.stockmanagement.databinding.FragmentTransactionBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TransactionFragment : Fragment() {

    private var _binding: FragmentTransactionBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TransactionViewModel by viewModels()
    private var transactionAdapter = TransactionAdapter(emptyList())
    private var checkedElement: Int = -1
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTransactionBinding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.transactionList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = transactionAdapter
        }
        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.navigation_transaction_management)
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.toolbar_filter_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_filter -> {
                        val filterView = requireActivity().findViewById<View>(R.id.action_filter)
                        val popup = PopupMenu(requireContext(), filterView)
                        popup.menuInflater.inflate(R.menu.filter_menu_transactions, popup.menu)
                        popup.menu.findItem(checkedElement)?.isChecked = true
                        popup.setOnMenuItemClickListener {

                            when (it.itemId) {
                                R.id.filter_all -> {
                                    viewModel.filterResults(null, R.id.filter_all)
                                    true
                                }

                                R.id.filter_restockk -> {
                                    viewModel.filterResults(Type.RESTOCK, R.id.filter_restockk)
                                    true
                                }

                                R.id.filter_sale -> {
                                    viewModel.filterResults(Type.SALE, R.id.filter_sale)
                                    true
                                }

                                else -> false
                            }
                        }
                        popup.show()
                        true
                    }

                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    checkedElement = uiState.checkedElement
                    binding.progressBar.isVisible = uiState.isLoading
                    transactionAdapter.updateData(uiState.transactions)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}