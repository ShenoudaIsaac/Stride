package com.shenouda.stride.feature_auth.data.datasource.remote

import com.shenouda.stride.common.domain.model.Role
import com.shenouda.stride.common.domain.model.User
import com.shenouda.stride.feature_auth.data.model.AuthUserDto
import kotlinx.coroutines.flow.Flow

interface AuthRemoteDataSource {
    val authStateFlow: Flow<AuthUserDto?>

    suspend fun getCurrentUser(): AuthUserDto?

    suspend fun login(email: String, password: String): AuthUserDto
    suspend fun register(
        email: String,
        password: String,
        displayName: String,
        role: Role,
    ): AuthUserDto
    /**
     * Sign in or register via Google.
     * @param idToken  The Google ID token from [Google SignIn Helper.getIdToken].
     * @param role     Required only when this is a first-time registration.
     *                 For returning users the role is fetched from Firestore.
     */
    suspend fun signInWithGoogle(idToken:String, role:Role?): AuthUserDto

    suspend fun sendPasswordResetEmail(email: String)

    suspend fun sendEmailVerification()

    suspend fun logout()
}