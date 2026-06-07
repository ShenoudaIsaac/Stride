package com.shenouda.stride.feature_auth.data.repository_impl

import com.shenouda.stride.common.domain.model.Role
import com.shenouda.stride.common.domain.model.User
import com.shenouda.stride.feature_auth.data.datasource.remote.AuthRemoteDataSource
import com.shenouda.stride.feature_auth.data.model.toDomain
import com.shenouda.stride.feature_auth.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val remoteDataSource: AuthRemoteDataSource
): AuthRepository {
    override val authState: Flow<User?> =
        remoteDataSource.authStateFlow.map { authUserDto -> authUserDto?.toDomain() }
    override suspend fun getCurrentUser(): User? {
        return remoteDataSource.getCurrentUser()?.toDomain()
    }

    override suspend fun login(
        email: String,
        password: String
    ): Result<User> {
        return runCatching { remoteDataSource.login(email, password).toDomain() }
    }

    override suspend fun register(
        email: String,
        password: String,
        displayName: String,
        role: Role
    ): Result<User> {
        return runCatching {
            remoteDataSource.register(email, password, displayName, role).toDomain()
        }
    }

    override suspend fun signInWithGoogle(
        idToken: String,
        role: Role?
    ): Result<User> {
        return runCatching { remoteDataSource.signInWithGoogle(idToken, role).toDomain() }
    }

    override suspend fun updateRole(role: Role): Result<Unit> {
        return runCatching { remoteDataSource.updateRole(role) }
    }

    override suspend fun sendPasswordResetEmail(email: String): Result<Unit> {
        return runCatching { remoteDataSource.sendPasswordResetEmail(email) }
    }

    override suspend fun sendEmailVerification(): Result<Unit> {
        return runCatching {
            remoteDataSource.sendEmailVerification()
        }
    }

    override suspend fun logout(): Result<Unit> {
        return runCatching { remoteDataSource.logout() }
    }
}