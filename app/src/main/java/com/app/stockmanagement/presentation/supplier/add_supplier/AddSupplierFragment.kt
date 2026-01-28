package com.app.stockmanagement.presentation.supplier.add_supplier

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.app.stockmanagement.R
import com.app.stockmanagement.databinding.FragmentAddSupplierBinding
import com.app.stockmanagement.domain.model.Supplier
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddSupplierFragment : DialogFragment() {
    private val viewModel: AddSupplierFragmentViewModel by viewModels()
    private var _binding: FragmentAddSupplierBinding? = null
    private val binding get() = _binding!!
    private lateinit var listOfFields: List<TextInputEditText>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(
            STYLE_NORMAL, com.google.android.material.R.style.Theme_Material3_DayNight_NoActionBar
        )

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.topAppBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        listOfFields.map { it.doAfterTextChanged { validateForm() } }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.eventFlow.collect { event ->
                    when (event) {
                        is AddSupplierUiEffect.ShowError -> Snackbar.make(
                            binding.root, event.message, Snackbar.LENGTH_LONG
                        ).setBackgroundTint(Color.RED).show()

                        AddSupplierUiEffect.Success -> findNavController().popBackStack()
                    }
                }
            }


        }
        binding.topAppBar.setOnMenuItemClickListener {
            val supplier = Supplier(
                id = 0,
                name = binding.name.text.toString(),
                contactPerson = binding.contactPerson.text.toString(),
                phone = binding.phone.text.toString(),
                email = binding.email.text.toString(),
                address = binding.address.text.toString(),
            )
            viewModel.addSupplier(supplier)
            return@setOnMenuItemClickListener true
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddSupplierBinding.inflate(inflater, container, false)
        handleSaveBtn(false)
        listOfFields = with(binding) { listOf(name, contactPerson, phone, email, address) }
        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun handleSaveBtn(isEnabled: Boolean) =
        binding.topAppBar.menu.findItem(R.id.action_save)?.let { btn -> btn.isEnabled = isEnabled }


    private fun validateForm() =
        listOfFields.all { it.text?.toString()?.trim()?.isNotBlank() == true }
            .let { handleSaveBtn(it) }
}