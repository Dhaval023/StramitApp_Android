package com.example.stramitapp.ui.login

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.stramitapp.R
import com.example.stramitapp.databinding.FragmentLoginBinding
import com.example.stramitapp.utilities.AppSettings
import kotlinx.coroutines.launch
import java.io.File

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

            // C#: isAuthenticated == 1 → navigate to RootPage
            is LoginUiState.Success -> {
                setLoading(false)
                viewModel.resetState()
//                if (hasStoragePermission()) {
//                    initFoldersAndNavigate()   // already granted — go straight to home
//                } else {
//                    requestStoragePermission() // ask first, navigate in result callback
//                }
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
    private val STORAGE_REQUEST_CODE = 101

//    private fun requestStoragePermission() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            // Android 11+ — opens system settings, user toggles manually
//            try {
//                startActivityForResult(
//                    Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION).apply {
//                        data = Uri.parse("package:${requireContext().packageName}")
//                    },
//                    STORAGE_REQUEST_CODE
//                )
//            } catch (e: Exception) {
//                startActivityForResult(
//                    Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION),
//                    STORAGE_REQUEST_CODE
//                )
//            }
//        } else {
//            // Android 10 and below — normal popup
//            requestPermissions(
//                arrayOf(
//                    Manifest.permission.READ_EXTERNAL_STORAGE,
//                    Manifest.permission.WRITE_EXTERNAL_STORAGE
//                ),
//                STORAGE_REQUEST_CODE
//            )
//        }
//    }

// ── Android 11+ result (back from settings screen) ───────────────────────

//     override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == STORAGE_REQUEST_CODE) {
//            if (hasStoragePermission()) {
//                initFoldersAndNavigate()
//            } else {
//                showPermissionDeniedDialog()
//            }
//        }
//    }

// ── Android 10 and below result (popup response) ─────────────────────────

//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if (requestCode == STORAGE_REQUEST_CODE) {
//            if (grantResults.isNotEmpty() &&
//                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                initFoldersAndNavigate()
//            } else {
//                showPermissionDeniedDialog()
//            }
//        }
//    }

// ── Create folders then navigate ──────────────────────────────────────────

    private fun initFoldersAndNavigate() {
        // Create all required folders now that permission is confirmed
        File(AppSettings.pathDatabase).mkdirs()

        val imagesFolder = AppSettings.pathImages

        val newImages    = File(imagesFolder, "AssetNewImages")
        val issueImages  = File(imagesFolder, "AssetIssueImages")
        val returnImages = File(imagesFolder, "AssetReturnImages")

        if (!newImages.exists())    newImages.mkdirs()
        if (!issueImages.exists())  issueImages.mkdirs()
        if (!returnImages.exists()) returnImages.mkdirs()

        AppSettings.pathAssetNewImages    = newImages.absolutePath    + "/"
        AppSettings.pathAssetIssueImages  = issueImages.absolutePath  + "/"
        AppSettings.pathAssetReturnImages = returnImages.absolutePath + "/"

        // Navigate to home
        findNavController().navigate(R.id.action_nav_login_to_nav_home)
    }

// ── Permission denied dialog ──────────────────────────────────────────────

//    private fun showPermissionDeniedDialog() {
//        AlertDialog.Builder(requireContext())
//            .setTitle("Permission Required")
//            .setMessage(
//                "Storage permission is required to store and sync data. " +
//                        "Please grant it to continue."
//            )
//            .setPositiveButton("Try Again") { _, _ -> requestStoragePermission() }
//            .setNegativeButton("Exit")      { _, _ -> requireActivity().finish() }
//            .setCancelable(false)
//            .show()
//    }
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