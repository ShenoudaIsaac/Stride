package com.shenouda.stride.feature_auth.domain.usecase

import com.shenouda.stride.feature_auth.domain.repository.AuthRepository
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val repository: AuthRepository,
) {
    suspend operator fun invoke(): Result<Unit> = repository.logout()
}