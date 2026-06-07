package com.shenouda.stride.feature_auth.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shenouda.stride.common.domain.model.Role
import com.shenouda.stride.feature_auth.data.datasource.local.AuthDataStore
import com.shenouda.stride.feature_auth.domain.usecase.LoginUseCase
import com.shenouda.stride.feature_auth.domain.usecase.LogoutUseCase
import com.shenouda.stride.feature_auth.domain.usecase.ObserveAuthStateUseCase
import com.shenouda.stride.feature_auth.domain.usecase.RegisterUseCase
import com.shenouda.stride.feature_auth.domain.usecase.SendPasswordResetEmailUseCase
import com.shenouda.stride.feature_auth.domain.usecase.SignInWithGoogleUseCase
import com.shenouda.stride.feature_auth.domain.usecase.UpdateRoleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuthUiState(
    val isLoading: Boolean=false,
    val errorMessage:String?= null
)
@HiltViewModel
class AuthViewModel@Inject constructor(
    private val authDataStore: AuthDataStore,
    private val observeAuthStateUseCase: ObserveAuthStateUseCase,
    private val registerUseCase: RegisterUseCase,
    private val signInWithGoogleUseCase: SignInWithGoogleUseCase,
    private val updateRoleUseCase: UpdateRoleUseCase,
    private val loginUseCase: LoginUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val sendPasswordResetEmailUseCase: SendPasswordResetEmailUseCase

): ViewModel() {

    val role: StateFlow<Role?> = authDataStore.selectedRole
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            observeAuthStateUseCase().collect { user ->
                if (user == null) authDataStore.clearRole()
            }
        }
    }

    fun signUpWithEmail(name: String, email: String, password: String, role: Role) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            registerUseCase(email, password, name, role)
                .onSuccess {
                    authDataStore.saveRole(role)
                    _uiState.update { it.copy(isLoading = false) }
                }
                .onFailure { error ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = error.message) }
                }
        }
    }

    fun signUpWithGoogle(idToken: String, role: Role?=null){
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            signInWithGoogleUseCase(idToken,role)
                .onSuccess {user->
                    authDataStore.saveRole(user.role)
                    _uiState.update { it.copy(isLoading = false) }
                }
                .onFailure { error->
                    _uiState.update {it.copy(isLoading = false, errorMessage = error.message)}
                }
        }
    }

    fun updateRoleSelection(role: Role) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            updateRoleUseCase(role)
                .onSuccess {
                    authDataStore.saveRole(role)
                    _uiState.update { it.copy(isLoading = false) }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = error.message)
                    }
                }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            loginUseCase(email, password)
                .onSuccess { user ->
                    authDataStore.saveRole(user.role)
                    _uiState.update { it.copy(isLoading = false) }
                }
                .onFailure { error ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = error.message) }
                }
        }
    }
    fun signOut() {
        viewModelScope.launch {
            logoutUseCase()
            authDataStore.clearRole()
        }
    }

    fun sendPasswordReset(email: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            sendPasswordResetEmailUseCase(email)
                .onSuccess { _uiState.update { it.copy(isLoading = false) } }
                .onFailure { error ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = error.message) }
                }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
