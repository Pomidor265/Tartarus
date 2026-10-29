package com.example.tartarus.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tartarus.data.AuthRepository
import com.example.tartarus.data.GameHistory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private val repo = AuthRepository()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    private val _username = MutableStateFlow<String?>(null)
    val username = _username.asStateFlow()

    private val _email = MutableStateFlow<String?>(null)
    val email = _email.asStateFlow()

    private val _history = MutableStateFlow<List<GameHistory>>(emptyList())
    val history = _history.asStateFlow()

    fun login(email: String, pass: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                repo.signIn(email, pass)
                onSuccess()
            } catch (e: Exception) {
                _error.value = e.message ?: "Ошибка входа"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun register(email: String, pass: String, username: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                repo.signUp(email, pass, username)
                onSuccess()
            } catch (e: Exception) {
                _error.value = e.message ?: "Ошибка регистрации"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadProfileData() {
        _username.value = repo.currentUsername()
        _email.value = repo.currentUserEmail()

        viewModelScope.launch {
            try {
                _history.value = repo.getGameHistory()
            } catch (e: Exception) {
                // Обработка ошибки
            }
        }
    }

    fun logout(onLogout: () -> Unit) {
        viewModelScope.launch {
            try {
                repo.signOut()
            } finally {
                onLogout()
            }
        }
    }

    fun isLoggedIn(): Boolean = repo.isLoggedIn()
}