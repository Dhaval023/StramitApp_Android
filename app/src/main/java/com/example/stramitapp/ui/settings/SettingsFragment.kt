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
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[SettingsViewModel::class.java]

        setupObservers()
        setupListeners()
    }

    private fun setupObservers() {
        viewModel.licenseKey.observe(viewLifecycleOwner) { key ->
            if (binding.licenseeKeyEdittext.text.toString() != key) {
                binding.licenseeKeyEdittext.setText(key)
            }
        }

        viewModel.rememberLicenseKey.observe(viewLifecycleOwner) { remember ->
            binding.rememberLicenseeKeyCheckbox.isChecked = remember
        }

        viewModel.versionText.observe(viewLifecycleOwner) { version ->
            binding.versionTextview.text = version
        }
    }

    private fun setupListeners() {
        binding.licenseeKeyEdittext.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.onLicenseKeyChanged(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        binding.rememberLicenseeKeyCheckbox.setOnCheckedChangeListener { _, isChecked ->
            viewModel.onRememberChanged(isChecked)
        }

        binding.saveButton.setOnClickListener {
            if (viewModel.saveSettings()) {
                Toast.makeText(requireContext(), "Settings saved successfully", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            } else {
                Toast.makeText(requireContext(), "Please enter a license key", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}