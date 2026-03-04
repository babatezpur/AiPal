package com.saptarshi.aipal.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saptarshi.aipal.data.repository.AuthRepository
import com.saptarshi.aipal.domain.model.User
import com.saptarshi.aipal.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _loginState = MutableStateFlow<Resource<User>?>(null)
    val loginState : StateFlow<Resource<User>?> = _loginState.asStateFlow()

    private val _signupState = MutableStateFlow<Resource<User>?>(null)
    val signupState : StateFlow<Resource<User>?> = _signupState.asStateFlow()

    val tokenState = authRepository.isLoggedIn()

    fun login(
        email: String,
        password: String
    ) {
        viewModelScope.launch {
            _loginState.value = Resource.Loading
            _loginState.value = authRepository.login(email, password)
        }
    }


    fun register(
        username: String,
        email: String,
        password: String
    ) {
        viewModelScope.launch {
            _signupState.value = Resource.Loading
            _signupState.value = authRepository.register(username, email, password)
        }
    }

    fun resetLoginState() {
        _loginState.value = null
    }

    fun resetSignupState() {
        _signupState.value = null
    }
}