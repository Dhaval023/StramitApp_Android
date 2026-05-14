package com.example.stramitapp.ui.login

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import android.os.Build
import android.provider.Settings
import android.net.Uri
import android.content.Intent
import com.example.stramitapp.App
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.stramitapp.R
import com.example.stramitapp.databinding.FragmentLoginBinding
import com.example.stramitapp.services.PasswordStorage
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LoginViewModel by activityViewModels()

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

        checkFreshInstall()
        loadSavedCredentials()
        setupClickListeners()
        observeViewModel()
    }

    private fun checkFreshInstall() {
        val context = requireContext()
        val storageKeys = com.example.stramitapp.models.Constants.StorageKeys
        if (storageKeys.isFreshInstall(context)) {
            storageKeys.clearAll(context)
            storageKeys.setFreshInstall(context, false)
        }
    }

    private fun loadSavedCredentials() {
        val context = requireContext()
        val isRemembered = com.example.stramitapp.models.Constants.StorageKeys.getRememberCredentials(context)

        if (isRemembered) {
            val savedUsername = com.example.stramitapp.models.Constants.StorageKeys.getUsername(context)
            val savedPassword = com.example.stramitapp.models.Constants.StorageKeys.getPassword(context)
            
            if (savedUsername.isNotEmpty()) {
                binding.usernameEditText.setText(savedUsername)
            }
            if (savedPassword.isNotEmpty()) {
                val decryptedPassword = PasswordStorage.decrypt(savedPassword)
                binding.passwordEditText.setText(decryptedPassword)
            }
        }
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
            is LoginUiState.Success -> {
                setLoading(false)
                viewModel.resetState()
                initializeDatabaseAndNavigate()
            }
            is LoginUiState.NoLicenseKey -> {
                setLoading(false)
                viewModel.resetState()
                showAlertDialog(
                    title = "ERROR!",
                    message = "Please configure your licensee settings in the Setting page."
                ) {
                    findNavController().navigate(R.id.nav_settings)
                }
            }
            is LoginUiState.ForceLoginRequired -> {
                setLoading(false)
                viewModel.resetState()
                showForceLoginDialog(state.message)
            }
            is LoginUiState.ShowDialog -> {
                setLoading(false)
                viewModel.resetState()
                showAlertDialog(title = state.title, message = state.message)
            }
        }
    }

    private fun initializeDatabaseAndNavigate() {
        val app = requireActivity().application as App
        app.initializeDatabase()
        val bundle = Bundle().apply { putBoolean("fromLogin", true) }
        findNavController().navigate(R.id.action_nav_login_to_nav_home, bundle)
    }

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

    private fun showForceLoginDialog(message: String) {
        AlertDialog.Builder(requireContext())
            .setTitle("LOGIN")
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
                viewModel.forceLogin()
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