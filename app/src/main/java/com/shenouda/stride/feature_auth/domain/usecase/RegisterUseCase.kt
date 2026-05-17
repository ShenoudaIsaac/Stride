package com.shenouda.stride.feature_auth.domain.usecase

import com.shenouda.stride.common.domain.model.Role
import com.shenouda.stride.common.domain.model.User
import com.shenouda.stride.feature_auth.domain.repository.AuthRepository
import javax.inject.Inject

class RegisterUseCase {
    class RegisterUseCase @Inject constructor(
        private val authRepository: AuthRepository,
    ) {
        suspend operator fun invoke(
            email: String,
            password: String,
            displayName: String,
            role: Role,
        ): Result<User> {
            if (displayName.isBlank()) return Result.failure(IllegalArgumentException("Name is required"))
            if (email.isBlank()) return Result.failure(IllegalArgumentException("Email is required"))
            if (password.length < 8) return Result.failure(IllegalArgumentException("Password must be at least 8 characters"))
            if (role == Role.NONE) return Result.failure(IllegalArgumentException("Please select a role"))
            return authRepository.register(email.trim(), password, displayName.trim(), role)
        }
    }
}