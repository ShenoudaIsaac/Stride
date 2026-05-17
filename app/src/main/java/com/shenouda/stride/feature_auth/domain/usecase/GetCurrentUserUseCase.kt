package com.shenouda.stride.feature_auth.domain.usecase

import com.shenouda.stride.common.domain.model.User
import com.shenouda.stride.feature_auth.domain.repository.AuthRepository
import javax.inject.Inject

class GetCurrentUserUseCase @Inject constructor(
    private val repository: AuthRepository,
) {
    suspend operator fun invoke(): User? = repository.getCurrentUser()
}
