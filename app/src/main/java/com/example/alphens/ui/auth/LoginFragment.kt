package com.example.alphens.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.alphens.R
import com.example.alphens.databinding.FragmentLoginBinding
import com.example.alphens.util.Resource
import com.example.alphens.util.hide
import com.example.alphens.util.isValidEmail
import com.example.alphens.util.show
import com.example.alphens.util.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentLoginBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observer()
        binding.btnLogin.setOnClickListener {
            if (validation()) {
                authViewModel.login(
                    email = binding.etUsername.text.toString(),
                    password = binding.etPassword.text.toString()
                )
            }
        }

        binding.forgotPassword.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_forgotPasswordFragment)
        }

        binding.signupText.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signupFragment)
        }
    }

    fun observer() {
        authViewModel.login.observe(viewLifecycleOwner) { state ->
            when (state) {
                is Resource.Loading -> {
                    binding.btnLogin.setText("")
                    binding.pbLogin.show()
                }
                is Resource.Error -> {
                    binding.btnLogin.setText("Login")
                    binding.pbLogin.hide()
                    state.error?.let { toast(requireContext(), it) }
                }
                is Resource.Success -> {
                    binding.btnLogin.setText("Login")
                    binding.pbLogin.hide()
                    toast(requireContext(), state.data)
                    findNavController().navigate(R.id.action_loginFragment_to_nav_graph)
                }
            }
        }
    }

    fun validation(): Boolean {
        var isValid = true

        if (binding.etUsername.text.isNullOrEmpty()) {
            isValid = false
            toast(requireContext(), "Enter Email")
        } else {
            if (!binding.etUsername.text.toString().isValidEmail()) {
                isValid = false
                toast(requireContext(), "invalid email")
            }
        }
        if (binding.etPassword.text.isNullOrEmpty()) {
            isValid = false
            toast(requireContext(), "Enter Password")
        } else {
            if (binding.etPassword.text.toString().length < 8) {
                isValid = false
                toast(requireContext(), "invalid password")
            }
        }
        return isValid
    }

    override fun onStart() {
        super.onStart()
        authViewModel.getSession { user ->
            if (user != null) {
                findNavController().navigate(R.id.action_loginFragment_to_nav_graph)
            }
        }
    }
}