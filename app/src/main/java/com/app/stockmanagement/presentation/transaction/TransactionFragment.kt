package com.app.stockmanagement.presentation.transaction

import android.app.Activity
import android.graphics.Color
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
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
import com.app.stockmanagement.databinding.PdfItemLayoutBinding
import com.app.stockmanagement.databinding.PdfLayoutBinding
import com.app.stockmanagement.domain.model.TransactionWithProduct
import com.app.stockmanagement.util.Constants.ERROR
import com.app.stockmanagement.util.Constants.TRANSACTION_REPORT_NAME
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Locale

private const val s = "TransactionReport.pdf"

@AndroidEntryPoint
class TransactionFragment : Fragment() {

    private var _binding: FragmentTransactionBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TransactionViewModel by viewModels()
    private var transactionAdapter = TransactionAdapter(emptyList())
    private var checkedElement: Int = -1
    private lateinit var transactionList: List<TransactionWithProduct>
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
                menuInflater.inflate(R.menu.toolbar_filter_and_export_menu, menu)
                menu.findItem(R.id.action_export).isVisible = true
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

                    R.id.action_export -> {
                        generateReport(transactionList)
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
                    transactionList = uiState.transactions
                    binding.progressBar.isVisible = uiState.isLoading
                    transactionAdapter.updateData(uiState.transactions)
                }
            }
        }
    }

    private fun generateReport(transactions: List<TransactionWithProduct>) {
        val inflater = LayoutInflater.from(requireContext())
        val root = PdfLayoutBinding.inflate(inflater, null, false)
        val container = root.transactionContainer

        transactions.forEach { item ->
            val row = PdfItemLayoutBinding.inflate(inflater, null, false)

            row.pdfItemName.text = item.productName
            row.pdfBarcode.text = item.productBarcode
            row.pdfQty.text = item.quantity.toString()
            row.pdfPricePerUnit.text = "${item.productPrice.toInt()} $"
            row.pdfDate.text =
                SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(item.date)
            container.addView(row.root)
        }

        layoutToPdf(root.root, requireActivity())
    }

    private fun layoutToPdf(view: View, activity: Activity) {

        val document = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        val page = document.startPage(pageInfo)

        val widthMeasureSpec =
            View.MeasureSpec.makeMeasureSpec(pageInfo.pageWidth, View.MeasureSpec.EXACTLY)
        val heightMeasureSpec =
            View.MeasureSpec.makeMeasureSpec(pageInfo.pageHeight, View.MeasureSpec.EXACTLY)

        view.measure(widthMeasureSpec, heightMeasureSpec)
        view.layout(0, 0, pageInfo.pageWidth, pageInfo.pageHeight)
        val canvas = page.canvas
        view.draw(canvas)

        document.finishPage(page)

        val file = File(activity.getExternalFilesDir(null), TRANSACTION_REPORT_NAME)
        try {
            document.writeTo(FileOutputStream(file))
            Toast.makeText(activity, "PDF Saved: ${file.absolutePath}", Toast.LENGTH_LONG).show()
            document.close()
        } catch (e: IOException) {
            Snackbar.make(
                binding.root, ERROR, Snackbar.LENGTH_LONG
            ).setBackgroundTint(Color.RED).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}