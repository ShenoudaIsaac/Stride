package com.shenouda.stride.feature_auth.domain.repository

import com.shenouda.stride.common.domain.model.Role
import com.shenouda.stride.common.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    /** Emits the current user whenever Firebase auth state changes. Null = signed out. */
    val authState: Flow<User?>

    suspend fun getCurrentUser(): User?

    suspend fun login(email: String, password: String): Result<User>
    suspend fun register(
        email: String,
        password: String,
        displayName: String,
        role: Role,
    ): Result<User>
    /**
     * @param idToken  Google ID token from the Credential Manager picker.
     * @param role     Only needed on first sign-in (new user). Null for returning users.
     */
    suspend fun signInWithGoogle(idToken:String, role:Role?): Result<User>
    suspend fun updateRole(role: Role): Result<Unit>
    suspend fun sendPasswordResetEmail(email: String): Result<Unit>

    suspend fun sendEmailVerification(): Result<Unit>

    suspend fun logout(): Result<Unit>
}