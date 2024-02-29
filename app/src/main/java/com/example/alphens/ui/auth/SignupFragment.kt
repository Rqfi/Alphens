package com.example.alphens.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.alphens.R
import com.example.alphens.data.model.User
import com.example.alphens.databinding.FragmentSignupBinding
import com.example.alphens.util.Resource
import com.example.alphens.util.hide
import com.example.alphens.util.isValidEmail
import com.example.alphens.util.show
import com.example.alphens.util.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignupFragment : Fragment() {

    private lateinit var binding: FragmentSignupBinding
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentSignupBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observer()
        binding.btnSignup.setOnClickListener {
            if (validation()) {
                authViewModel.register(
                    email = binding.etEmail.text.toString(),
                    password = binding.etPassword.text.toString(),
                    user = getUserObj()
                )
            }
        }

        binding.loginText.setOnClickListener {
            findNavController().navigate(R.id.action_signupFragment_to_loginFragment)
        }
    }

    fun observer() {
        authViewModel.register.observe(viewLifecycleOwner) { state ->
            when (state) {
                is Resource.Loading -> {
                    binding.btnSignup.setText("Loading...")
                    binding.pbRegister.show()
                }
                is Resource.Error -> {
                    binding.btnSignup.setText("Register")
                    binding.pbRegister.hide()
                    state.error?.let { toast(requireContext(), it) }
                }
                is Resource.Success -> {
                    binding.btnSignup.setText("Register")
                    binding.pbRegister.hide()
                    toast(requireContext(), state.data)
                    findNavController().navigate(R.id.action_signupFragment_to_loginFragment)
                }
            }
        }
    }

    fun getUserObj(): User {
        return User(
            id = "",
            name = binding.etName.text.toString(),
            email = binding.etEmail.text.toString()
        )
    }

    fun validation(): Boolean {
        var isValid = true

        if (binding.etName.text.isNullOrEmpty()) {
            isValid = false
            toast(requireContext(), "Enter name")
        }

        if (binding.etEmail.text.isNullOrEmpty()) {
            isValid = true
            toast(requireContext(), "Enter email")
        } else {
            if (!binding.etEmail.text.toString().isValidEmail()) {
                isValid = false
                toast(requireContext(), "invalid email")
            }
        }

        if (binding.etPassword.text.isNullOrEmpty()) {
            isValid = false
            toast(requireContext(), "Enter password")
        } else {
            if (binding.etPassword.text.toString().length < 8) {
                isValid = true
                toast(requireContext(), "invalid password")
            }
        }
        return isValid
    }
}