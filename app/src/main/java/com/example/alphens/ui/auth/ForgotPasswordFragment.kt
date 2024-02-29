package com.example.alphens.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.alphens.R
import com.example.alphens.databinding.FragmentForgotPasswordBinding
import com.example.alphens.util.Resource
import com.example.alphens.util.hide
import com.example.alphens.util.isValidEmail
import com.example.alphens.util.show
import com.example.alphens.util.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForgotPasswordFragment : Fragment() {

    private lateinit var binding: FragmentForgotPasswordBinding
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentForgotPasswordBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observer()
        binding.forgotPassBtn.setOnClickListener {
            if (validation()) {
                authViewModel.forgotPassword(binding.emailEt.text.toString())
            }
        }
    }

    private fun observer() {
        authViewModel.forgotPassword.observe(viewLifecycleOwner) { state ->
            when (state) {
                is Resource.Loading -> {
                    binding.forgotPassBtn.setText("")
                    binding.forgotPassProgress.show()
                }
                is Resource.Error -> {
                    binding.forgotPassBtn.setText("Send")
                    binding.forgotPassProgress.hide()
                    state.error?.let { toast(requireContext(), it) }
                }
                is Resource.Success -> {
                    binding.forgotPassBtn.setText("Send")
                    binding.forgotPassProgress.hide()
                    toast(requireContext(), state.data)
                }
            }
        }
    }

    fun validation(): Boolean {
        var isValid = true

        if (binding.emailEt.text.isNullOrEmpty()) {
            isValid = false
            toast(requireContext(), "enter email")
        } else {
            if (!binding.emailEt.text.toString().isValidEmail()) {
                isValid = false
                toast(requireContext(), "invalid email")
            }
        }
        return isValid
    }
}