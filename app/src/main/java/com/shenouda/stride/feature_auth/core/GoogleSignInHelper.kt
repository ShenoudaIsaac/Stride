package com.shenouda.stride.feature_auth.core

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential

/**
 * Handles the Credential Manager flow to retrieve a Google ID token.
 *
 * Lives in the UI layer because [androidx.credentials.CredentialManager.getCredential] needs
 * an Activity context to show the account picker sheet.
 *
 * Usage in a composable:
 *   val context = LocalContext.current
 *   val scope   = rememberCoroutineScope()
 *   scope.launch {
 *       val token = GoogleSignInHelper.getIdToken(context, webClientId)
 *       token?.let { viewModel.signInWithGoogle(it) }
 *   }
 */
object GoogleSignInHelper {

    /**
     * Shows the Google account picker and returns the ID token on success,
     * or null if the user cancelled or an error occurred.
     */
    suspend fun getIdToken(
        context: Context,
        webClientId: String,
    ): String? {
        return try {
            val credentialManager = CredentialManager.Companion.create(context)

            val googleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)   // show all accounts, not just previously used
                .setServerClientId(webClientId)
                .setAutoSelectEnabled(false)            // don't auto-select if only one account
                .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            val result = credentialManager.getCredential(
                request = request,
                context = context,
            )

            // Extract the Google ID token from the credential
            GoogleIdTokenCredential.Companion
                .createFrom(result.credential.data)
                .idToken

        } catch (e: GetCredentialCancellationException) {
            null    // user dismissed the picker — not an error
        } catch (e: Exception) {
            null    // log e.message in production
        }
    }
}