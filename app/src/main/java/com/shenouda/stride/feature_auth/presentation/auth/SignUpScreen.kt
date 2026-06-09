package com.shenouda.stride.feature_auth.presentation.auth

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.shenouda.stride.R
import com.shenouda.stride.common.domain.model.Role
import com.shenouda.stride.feature_auth.core.GoogleSignInHelper
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    viewModel: AuthViewModel,
    onLoginClick: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val webClientId = stringResource(R.string.default_web_client_id)

    var step by remember { mutableStateOf(1) }
    var selectedRole by remember { mutableStateOf<Role?>(null) }
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearError()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background,
    ) { innerPadding ->
        AnimatedContent(
            targetState = step,
            transitionSpec = { slideInHorizontally { it } togetherWith slideOutHorizontally { -it } },
            label = "signup_step",
        ) { currentStep ->
            when (currentStep) {

                // ── Step 1: Role selection ──────────────────────────────────
                1 -> Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 24.dp),
                    verticalArrangement = Arrangement.Center,
                ) {
                    Spacer(Modifier.height(32.dp))

                    AuthHeader(
                        title = "Create your account",
                        subtitle = "How will you use Stride?",
                    )

                    Spacer(Modifier.height(32.dp))

                    RoleCard(
                        icon = "👩‍🏫",
                        title = "I'm a Teacher",
                        subtitle = "Upload content & manage students",
                        selected = selectedRole == Role.TEACHER,
                        onClick = { selectedRole = Role.TEACHER },
                    )
                    Spacer(Modifier.height(12.dp))
                    RoleCard(
                        icon = "🎓",
                        title = "I'm a Student",
                        subtitle = "Watch, learn & submit homework",
                        selected = selectedRole == Role.STUDENT,
                        onClick = { selectedRole = Role.STUDENT },
                    )

                    Spacer(Modifier.height(28.dp))

                    // Continue to email/password form
                    PrimaryGradientButton(
                        text = "Continue with email",
                        enabled = selectedRole != null,
                        onClick = { step = 2 },
                    )

                    Spacer(Modifier.height(20.dp))

                    OrDivider()

                    Spacer(Modifier.height(20.dp))

                    // Google Sign-Up (only enabled after role picked)
                    GoogleButton(
                        text = "Continue with Google",
                        enabled = selectedRole != null && !uiState.isLoading,
                        onClick = {
                            scope.launch {
                                val token = GoogleSignInHelper.getIdToken(
                                    context = context,
                                    webClientId = webClientId,
                                )
                                if (token != null) {
                                    // Register flow: pass the selected role so Firebase can save it
                                    viewModel.signUpWithGoogle(
                                        idToken = token,
                                        role = selectedRole,
                                    )
                                }
                            }
                        },
                    )

                    Spacer(Modifier.height(24.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            "Already have an account?",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        TextButton(onClick = onLoginClick) {
                            Text("Login", fontWeight = FontWeight.SemiBold)
                        }
                    }

                    Spacer(Modifier.height(16.dp))
                }

                // ── Step 2: Email / password form ───────────────────────────
                2 -> Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 24.dp),
                    verticalArrangement = Arrangement.Center,
                ) {
                    Box(Modifier.fillMaxWidth()) {
                        IconButton(onClick = { step = 1 }) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                            )
                        }
                    }

                    Spacer(Modifier.height(8.dp))

                    AuthHeader(
                        title = "Your details",
                        subtitle = "Signing up as ${selectedRole?.name?.lowercase()?.replaceFirstChar { it.uppercase() }}",
                        showLogo = false,
                    )

                    Spacer(Modifier.height(28.dp))

                    AuthTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = "Full name",
                    )
                    Spacer(Modifier.height(14.dp))

                    AuthTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = "Email",
                        leadingIcon = painterResource(R.drawable.ic_email),
                        keyboardType = KeyboardType.Email,
                    )
                    Spacer(Modifier.height(14.dp))

                    PasswordField(
                        value = password,
                        onValueChange = { password = it },
                        label = "Password",
                        isVisible = isPasswordVisible,
                        onToggleVisibility = { isPasswordVisible = !isPasswordVisible },
                    )

                    Spacer(Modifier.height(28.dp))

                    PrimaryGradientButton(
                        text = "Create account",
                        enabled = !uiState.isLoading,
                        loading = uiState.isLoading,
                        onClick = { viewModel.signUpWithEmail(name, email, password, selectedRole!!) },
                    )

                    Spacer(Modifier.height(16.dp))
                }
            }
        }
    }
}