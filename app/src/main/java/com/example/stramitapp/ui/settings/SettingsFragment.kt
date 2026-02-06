package com.example.stramitapp.ui.settings

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.stramitapp.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: SettingsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModel = ViewModelProvider(this)
            .get(SettingsViewModel::class.java)

        _binding = FragmentSettingsBinding.inflate(inflater, container, false)

        setupObservers()
        setupListeners()

        return binding.root
    }

    private fun setupObservers() {

        viewModel.licenseKey.observe(viewLifecycleOwner) {
            if (binding.licenseeKeyEdittext.text.toString() != it) {
                binding.licenseeKeyEdittext.setText(it)
            }
        }

        viewModel.rememberLicenseKey.observe(viewLifecycleOwner) {
            binding.rememberLicenseeKeyCheckbox.isChecked = it
        }

        viewModel.versionText.observe(viewLifecycleOwner) {
            binding.versionTextview.text = it
        }
    }

    private fun setupListeners() {

        binding.licenseeKeyEdittext.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.onLicenseKeyChanged(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        binding.rememberLicenseeKeyCheckbox.setOnCheckedChangeListener { _, isChecked ->
            viewModel.onRememberChanged(isChecked)
        }

        binding.saveButton.setOnClickListener {
            val success = viewModel.saveSettings()

            if (success) {
                Toast.makeText(requireContext(), "Settings saved", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Please enter license key",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}