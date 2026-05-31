package com.shenouda.stride.feature_auth.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shenouda.stride.common.domain.model.Role
import com.shenouda.stride.feature_auth.data.datasource.local.AuthDataStore
import com.shenouda.stride.feature_auth.domain.usecase.GetCurrentUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class SplashDestination {
    object Loading : SplashDestination()
    object NewUser : SplashDestination()
    class ReturningUser(val role: Role) : SplashDestination()
}

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val authDataStore: AuthDataStore
) : ViewModel() {
    private val _destination = MutableStateFlow<SplashDestination>(SplashDestination.Loading)
    val destination: StateFlow<SplashDestination> = _destination.asStateFlow()

    init {
        checkSession()
    }

    private fun checkSession() {
        viewModelScope.launch {
            val firebaseUser = getCurrentUserUseCase()
            if (firebaseUser == null) {
                authDataStore.clearRole()
                _destination.value = SplashDestination.NewUser
                return@launch
            }
                val savedRole = authDataStore.selectedRole.first()
                when {
                    savedRole == null || savedRole == Role.NONE -> {
                        authDataStore.saveRole(firebaseUser.role)
                        _destination.value = SplashDestination.ReturningUser(firebaseUser.role)
                    }

                    else -> {
                        _destination.value = SplashDestination.ReturningUser(savedRole)
                    }
                }
            }
        }
}