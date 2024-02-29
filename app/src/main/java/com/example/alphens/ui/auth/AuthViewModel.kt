package com.example.alphens.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.alphens.data.model.User
import com.example.alphens.data.repository.auth.AuthRepository
import com.example.alphens.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
): ViewModel() {

    private val _register = MutableLiveData<Resource<String>>()
    val register: LiveData<Resource<String>>
        get() = _register

    private val _login = MutableLiveData<Resource<String>>()
    val login: LiveData<Resource<String>>
        get() = _login

    private val _forgotPassword = MutableLiveData<Resource<String>>()
    val forgotPassword: LiveData<Resource<String>>
        get() = _forgotPassword

    fun register(
        email: String,
        password: String,
        user: User
    ) {
        _register.value = Resource.Loading
        authRepository.registerUser(
            email = email,
            password = password,
            user = user
        ) {
            _register.value = it
        }
    }

    fun login(
        email: String,
        password: String
    ) {
        _login.value = Resource.Loading
        authRepository.loginUser(
            email, password
        ) {
            _login.value = it
        }
    }

    fun forgotPassword (email: String) {
        _forgotPassword.value = Resource.Loading
        authRepository.forgotPassword(email) {
            _forgotPassword.value = it
        }
    }

    fun logout (result: () -> Unit) {
        authRepository.logout(result)
    }

    fun getSession (result: (User?) -> Unit) {
        authRepository.getSession(result)
    }
}