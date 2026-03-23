package com.example.stramitapp.views

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.stramitapp.databinding.FragmentForgotPasswordBinding
import com.example.stramitapp.viewmodels.ForgotPasswordUiState
import com.example.stramitapp.viewmodels.ForgotPasswordViewModel
import kotlinx.coroutines.launch

class ForgotPasswordFragment : Fragment() {

    private var _binding: FragmentForgotPasswordBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ForgotPasswordViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentForgotPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.etUsername.doAfterTextChanged { text ->
            viewModel.usernameText.value = text?.toString() ?: ""
        }

        binding.btnRetrievePassword.setOnClickListener {
            viewModel.onRetrievePasswordClicked()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    when (state) {
                        is ForgotPasswordUiState.Idle -> {
                            showLoading(false)
                        }
                        is ForgotPasswordUiState.Loading -> {
                            showLoading(true)
                        }
                        is ForgotPasswordUiState.Success -> {
                            showLoading(false)
                            showDialog("SUCCESS!", state.message)
                        }
                        is ForgotPasswordUiState.Error -> {
                            showLoading(false)
                            showDialog("ERROR!", state.message)
                        }
                    }
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.loadingOverlay.visibility  = if (isLoading) View.VISIBLE else View.GONE
        binding.btnRetrievePassword.isEnabled = !isLoading
    }

    private fun showDialog(title: String, message: String) {
        AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
                viewModel.resetState()
            }
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}