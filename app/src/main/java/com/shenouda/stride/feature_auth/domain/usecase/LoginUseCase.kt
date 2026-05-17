package com.shenouda.stride.feature_auth.domain.usecase

import com.shenouda.stride.common.domain.model.User
import com.shenouda.stride.feature_auth.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    val authRepository: AuthRepository
) {
    suspend operator fun invoke(email:String,password:String): Result<User>{
        if (email.isBlank()) return Result.failure(IllegalArgumentException("Email is required"))
        if (password.isBlank()) return Result.failure(IllegalArgumentException("Password is required"))
        return authRepository.login(email.trim(),password)
    }
}