package com.example.stramitapp.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.stramitapp.R
import com.example.stramitapp.databinding.FragmentLoginBinding
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        setupClickListeners()
        observeViewModel()

        return binding.root
    }

    private fun setupClickListeners() {
        binding.loginButton.setOnClickListener {
            viewModel.login(
                binding.usernameEditText.text.toString(),
                binding.passwordEditText.text.toString()
            )
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                when (state) {
                    is LoginUiState.Loading -> {
                        binding.loginButton.isEnabled = false
                    }
                    is LoginUiState.Success -> {
                        findNavController()
                            .navigate(R.id.action_nav_login_to_nav_home)
                    }
                    is LoginUiState.NavigateToSettings -> {
                        // Assuming nav_settings is the destination ID for settings
                        // You might need to change this if your ID is different
                        try {
                            findNavController().navigate(R.id.nav_settings)
                        } catch (e: Exception) {
                            Toast.makeText(requireContext(), "Settings navigation failed. Check nav graph.", Toast.LENGTH_SHORT).show()
                        }
                        viewModel.resetState()
                    }
                    is LoginUiState.Error -> {
                        binding.loginButton.isEnabled = true
                        Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                        viewModel.resetState()
                    }
                    else -> {
                        binding.loginButton.isEnabled = true
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
