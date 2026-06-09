package com.shenouda.stride.feature_auth.data.datasource.remote

import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.shenouda.stride.common.domain.model.Role
import com.shenouda.stride.feature_auth.data.model.AuthUserDto
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseAuthDataSource @Inject constructor(val auth: FirebaseAuth, val firestore: FirebaseFirestore) : AuthRemoteDataSource {
    override val authStateFlow: Flow<AuthUserDto?> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { fa ->
            trySend(fa.currentUser?.toDto())
        }
        auth.addAuthStateListener(listener)
        awaitClose { auth.removeAuthStateListener(listener) }
    }


    override suspend fun getCurrentUser(): AuthUserDto? {
        val fbUser = auth.currentUser ?: return null
        val role = fetchRoleFromFirestore(fbUser.uid)
        return fbUser.toDto(role)
    }

    override suspend fun login(
        email: String,
        password: String
    ): AuthUserDto {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            val fbUser = result.user ?: error("Login succeeded but user is null")
            val role = fetchRoleFromFirestore(fbUser.uid)
            fbUser.toDto(role)
        } catch (e: FirebaseAuthUserCollisionException) {
            throw Exception("This email is registered with a different sign-in method. please use Google sign-in.")
        } catch (e: FirebaseAuthException) {
            throw mapFirebaseException(e)
        }
    }

    override suspend fun register(
        email: String,
        password: String,
        displayName: String,
        role: Role
    ): AuthUserDto {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val fbUser = result.user ?: error("Register succeeded but user is null")
            fbUser.updateProfile(
                UserProfileChangeRequest.Builder().setDisplayName(displayName).build()
            ).await()
            runCatching {
                fbUser.sendEmailVerification().await()
            }//I put it inside run catching because it is an additional step, if any internet error no problema shnsh,I will register without any catching any issue
            saveUserToFirestore(fbUser.uid,email,displayName,role)
            fbUser.toDto(role.name)
        } catch (e: FirebaseAuthUserCollisionException) {
            throw Exception("An account already exists with this email. Try signing in instead.")
        } catch (e: FirebaseAuthException) {
            throw mapFirebaseException(e)
        }
    }

    override suspend fun signInWithGoogle(
        idToken: String,
        role: Role?
    ): AuthUserDto {
        return try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            val result = auth.signInWithCredential(credential).await()
            val fbUser = result.user ?: error("Google sign-in succeeded but user is null")
            if (result.additionalUserInfo?.isNewUser==true ){
                val selectedRole = role?: Role.NONE
                saveUserToFirestore(fbUser.uid, fbUser.email.orEmpty(), fbUser.displayName.orEmpty(), selectedRole)
                fbUser.toDto(selectedRole.name)
            }else{
                val role =fetchRoleFromFirestore(fbUser.uid)
                fbUser.toDto(role)
            }
        }catch (e: FirebaseAuthUserCollisionException){
            throw Exception("This email is registered with email and password. Please use that to sign in.")
        } catch (e: FirebaseAuthException){
            throw mapFirebaseException(e)
        }
    }

    override suspend fun updateRole(role: Role) {
        val uid = auth.currentUser?.uid ?: error("No signed-in user")
        firestore.collection("users").document(uid).update("role", role.name).await()
    }


    override suspend fun sendPasswordResetEmail(email: String) {
        try { auth.sendPasswordResetEmail(email).await() }
        catch (e: FirebaseAuthException) { throw mapFirebaseException(e) }
    }

    override suspend fun sendEmailVerification() {
        auth.currentUser?.sendEmailVerification()?.await() ?: error("No signed-in user")
    }

    override suspend fun logout() = auth.signOut()

    private fun FirebaseUser.toDto(
        role: String = Role.NONE.name,
    ) = AuthUserDto(
        uid = uid,
        email = email.orEmpty(),
        displayName = displayName,
        photoUrl = photoUrl?.toString(),
        isEmailVerified = isEmailVerified,
        role = role,
    )
    // ── Firestore helpers ─────────────────────────────────────────────────────
    private suspend fun saveUserToFirestore(uid: String, email: String, displayName: String, role: Role) {
        firestore.collection("users").document(uid).set(
            mapOf(
                "uid"         to uid,
                "email"       to email,
                "displayName" to displayName,
                "role"        to role.name,
                "createdAt"   to Timestamp.now(),
            )
        ).await()
    }
    private suspend fun fetchRoleFromFirestore(uid: String): String =
        try {
            firestore.collection("users")
                .document(uid)
                .get()
                .await()
                .getString("role") ?: Role.NONE.name
        } catch (e: Exception) {
            Role.NONE.name
        }

    private fun mapFirebaseException(e: FirebaseAuthException): Exception {
       return Exception(
            when (e.errorCode) {
                "ERROR_INVALID_EMAIL" -> "Invalid email address"
                "ERROR_WRONG_PASSWORD" -> "Incorrect password"
                "ERROR_USER_NOT_FOUND" -> "No account found with this email"
                "ERROR_EMAIL_ALREADY_IN_USE" -> "An account already exists with this email"
                "ERROR_WEAK_PASSWORD" -> "Password is too weak"
                "ERROR_NETWORK_REQUEST_FAILED" -> "No internet connection"
                "ERROR_TOO_MANY_REQUESTS" -> "Too many attempts. Try again later"
                else -> e.message ?: "Authentication failed"
            }
        )
    }
}