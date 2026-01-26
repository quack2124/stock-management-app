package com.app.stockmanagement.presentation.supplier.edit_supplier

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.app.stockmanagement.databinding.FragmentEditSupplierFragmentBinding
import com.app.stockmanagement.domain.model.Supplier
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EditSupplierFragment : DialogFragment() {
    private val viewModel: EditSupplierFragmentViewModel by viewModels()
    private var _binding: FragmentEditSupplierFragmentBinding? = null
    private val binding get() = _binding!!
    private val args: EditSupplierFragmentArgs by navArgs()
    private lateinit var supplier: Supplier
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
                viewModel.eventFlow.collect { event ->
                    when (event) {
                        is EditSupplierUiEffect.ShowError -> Snackbar.make(
                            binding.root, event.message, Snackbar.LENGTH_LONG
                        ).setBackgroundTint(Color.RED).show()

                        EditSupplierUiEffect.Success -> findNavController().popBackStack()
                    }
                }
            }


        }

        supplier = args.currentSupplier
        with(binding) {
            name.setText(supplier.name)
            contactPerson.setText(supplier.contactPerson)
            phone.setText(supplier.phone)
            email.setText(supplier.email)
            address.setText(supplier.address)
        }

        binding.topAppBar.setOnMenuItemClickListener {
            supplier = supplier.copy(
                name = binding.name.text.toString(),
                contactPerson = binding.contactPerson.text.toString(),
                phone = binding.phone.text.toString(),
                email = binding.email.text.toString(),
                address = binding.address.text.toString(),

                )
            viewModel.updateSupplier(supplier)
            return@setOnMenuItemClickListener true
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditSupplierFragmentBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.topAppBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}