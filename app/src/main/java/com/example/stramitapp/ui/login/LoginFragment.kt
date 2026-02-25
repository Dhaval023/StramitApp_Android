package com.example.stramitapp.ui.login

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.usernameEditText.setText("mttest")
        binding.passwordEditText.setText("Mitesh@123")

        setupClickListeners()
        observeViewModel()
    }

    private fun setupClickListeners() {
        binding.loginButton.setOnClickListener {
            val username = binding.usernameEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()
            viewModel.login(username, password)
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    handleState(state)
                }
            }
        }
    }

    private fun handleState(state: LoginUiState) {
        when (state) {
            is LoginUiState.Idle -> setLoading(false)

            is LoginUiState.Loading -> setLoading(true)

            // C#: isAuthenticated == 1 → navigate to RootPage
            is LoginUiState.Success -> {
                setLoading(false)
                viewModel.resetState()
                findNavController().navigate(R.id.action_nav_login_to_nav_home)
            }

            // C#: !IsLicenseeKeyAvailable → ShowMessageDialog → navigate to Settings
            is LoginUiState.NoLicenseKey -> {
                setLoading(false)
                viewModel.resetState()
                showAlertDialog(
                    title   = "ERROR!",
                    message = "Please configure your licensee settings in the Setting page."
                ) {
                    findNavController().navigate(R.id.nav_settings)
                }
            }

            // C#: isAuthenticated == 3 → ShowForceLoginDialog with server message
            is LoginUiState.ForceLoginRequired -> {
                setLoading(false)
                viewModel.resetState()
                showForceLoginDialog(state.message)
            }

            // C#: else → ShowMessageDialog("LOGIN FAILED!", AppSettings.LoginErrorMessage)
            is LoginUiState.ShowDialog -> {
                setLoading(false)
                viewModel.resetState()
                showAlertDialog(
                    title   = state.title,
                    message = state.message
                )
            }
        }
    }

    // ✅ C# ShowMessageDialog equivalent — optional onDismiss action
    private fun showAlertDialog(
        title: String,
        message: String,
        onDismiss: (() -> Unit)? = null
    ) {
        AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
                onDismiss?.invoke()
            }
            .setCancelable(false)
            .show()
    }

    // ✅ C# ShowForceLoginDialog — shows server message, OK proceeds
    private fun showForceLoginDialog(message: String) {
        AlertDialog.Builder(requireContext())
            .setTitle("LOGIN")
            .setMessage(message)  // ✅ real server message
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
                viewModel.forceLogin()  // ✅ setForceFullAssign = true
            }
            .setCancelable(false)
            .show()
    }

    private fun setLoading(isLoading: Boolean) {
        binding.loginButton.isEnabled = !isLoading
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}