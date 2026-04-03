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
            is LoginUiState.Success -> {
                setLoading(false)
                viewModel.resetState()
                if (hasStoragePermission()) {
                    initializeDatabaseAndNavigate()
                } else {
                    requestStoragePermission()
                }
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

    private val STORAGE_REQUEST_CODE = 101

    private fun hasStoragePermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            android.os.Environment.isExternalStorageManager()
        } else {
            ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun requestStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            try {
                startActivityForResult(
                    Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION).apply {
                        data = Uri.parse("package:${requireContext().packageName}")
                    },
                    STORAGE_REQUEST_CODE
                )
            } catch (e: Exception) {
                startActivityForResult(
                    Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION),
                    STORAGE_REQUEST_CODE
                )
            }
        } else {
            requestPermissions(
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                STORAGE_REQUEST_CODE
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == STORAGE_REQUEST_CODE) {
            if (hasStoragePermission()) {
                initializeDatabaseAndNavigate()
            } else {
                showPermissionDeniedDialog()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == STORAGE_REQUEST_CODE) {
            if (grantResults.isNotEmpty() &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED
            ) {
                initializeDatabaseAndNavigate()
            } else {
                showPermissionDeniedDialog()
            }
        }
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

    private fun showPermissionDeniedDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Permission Required")
            .setMessage("Storage permission is required to store database and images.")
            .setPositiveButton("Try Again") { _, _ -> requestStoragePermission() }
            .setNegativeButton("Exit") { _, _ -> requireActivity().finish() }
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