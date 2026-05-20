package com.shenouda.stride.feature_auth.domain.usecase

import com.shenouda.stride.common.domain.model.Role
import com.shenouda.stride.common.domain.model.User
import com.shenouda.stride.feature_auth.domain.repository.AuthRepository
import javax.inject.Inject

class SignInWithGoogleUseCase @Inject constructor(
    private val repository: AuthRepository,
) {
    /**
     * @param idToken  The raw Google ID token string from [GoogleSignInHelper].
     * @param role     Pass the role the user selected if this is a new registration.
     *                 Pass null for returning users — role is fetched from Firestore.
     */
    suspend operator fun invoke(idToken: String, role: Role? = null): Result<User> {
        if (idToken.isBlank()) {
            return Result.failure(IllegalArgumentException("Google ID token is empty"))
        }
        return repository.signInWithGoogle(idToken, role)
    }
}