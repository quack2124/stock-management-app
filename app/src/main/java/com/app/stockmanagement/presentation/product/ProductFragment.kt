package com.app.stockmanagement.presentation.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.appcompat.widget.Toolbar
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
import com.app.stockmanagement.databinding.FragmentProductBinding
import com.app.stockmanagement.util.Constants.SEARCH_QUERY
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductFragment : Fragment() {

    private var _binding: FragmentProductBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProductViewModel by viewModels()
    private val productAdapter = ProductAdapter(emptyList())
    private var checkedElement = -1
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeSearch()
        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.toolbar_filter_and_export_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_search -> {
                        val toolbar =
                            requireActivity().findViewById<Toolbar>(R.id.topAppBar)
                        toolbar.menu.findItem(R.id.action_filter)?.isVisible = false
                        false
                    }

                    R.id.action_filter -> {
                        val filterView = requireActivity().findViewById<View>(R.id.action_filter)
                        val popup = PopupMenu(requireContext(), filterView)
                        popup.menuInflater.inflate(R.menu.filter_menu_products, popup.menu)
                        popup.menu.findItem(checkedElement)?.isChecked = true
                        popup.setOnMenuItemClickListener {

                            viewModel.filterResults(it.title.toString(), it.itemId)
                            true
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
                    binding.progressBarProduct.isVisible = uiState.isLoading
                    productAdapter.updateData(uiState.products)
                }
            }
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentProductBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.fabAdd.setOnClickListener { findNavController().navigate(R.id.navigation_add_product) }
        binding.productList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = productAdapter
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeSearch() {
        findNavController().currentBackStackEntry?.savedStateHandle
            ?.getLiveData<String>(SEARCH_QUERY)
            ?.observe(viewLifecycleOwner) { query ->
                viewModel.search(query)
            }
    }

}