package com.shenouda.stride.feature_auth.domain.usecase

import com.shenouda.stride.common.domain.model.User
import com.shenouda.stride.feature_auth.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveAuthStateUseCase @Inject constructor(
    private val repository: AuthRepository,
) {
    operator fun invoke(): Flow<User?> = repository.authState
}