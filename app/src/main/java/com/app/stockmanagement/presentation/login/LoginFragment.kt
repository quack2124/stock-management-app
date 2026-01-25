package com.app.stockmanagement.presentation.login

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.app.stockmanagement.R
import com.app.stockmanagement.databinding.FragmentLoginBinding
import com.app.stockmanagement.util.Constants
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val viewModel: LoginViewModel by viewModels()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.loginEvent.collect { event ->
                    when (event) {
                        is LoginUiEvent.Success -> {
                            findNavController().navigate(R.id.navigation_dashboard)
                            val sharedPref = activity?.getSharedPreferences(
                                Constants.SHARED_PREF_KEY, Context.MODE_PRIVATE
                            )
                            sharedPref?.edit {
                                putBoolean(Constants.IS_SIGNED_IN_KEY, true)
                            }

                        }

                        is LoginUiEvent.Error -> {
                            Snackbar.make(binding.root, event.message, Snackbar.LENGTH_LONG)
                                .setBackgroundTint(Color.RED)
                                .show()
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


        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.btnLogIn.setOnClickListener {
            viewModel.login(
                username = binding.username.text.toString(),
                password = binding.password.text.toString()
            )
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}