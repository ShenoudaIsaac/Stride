package com.shenouda.stride.feature_auth.domain.usecase

import com.shenouda.stride.feature_auth.domain.repository.AuthRepository
import javax.inject.Inject

class SendPasswordResetEmailUseCase @Inject constructor(
    private val repository: AuthRepository,
) {
    suspend operator fun invoke(email: String): Result<Unit> {
        if (email.isBlank()) return Result.failure(IllegalArgumentException("Email is required"))
        return repository.sendPasswordResetEmail(email.trim())
    }
}