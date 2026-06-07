package com.shenouda.stride.feature_auth.domain.usecase

import com.shenouda.stride.common.domain.model.Role
import com.shenouda.stride.feature_auth.domain.repository.AuthRepository
import javax.inject.Inject

class UpdateRoleUseCase @Inject constructor(val authRepository: AuthRepository) {
    suspend operator fun invoke(role: Role): Result<Unit> {
        return authRepository.updateRole(role)
    }
}