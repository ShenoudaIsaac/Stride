package com.shenouda.stride.feature_auth.presentation.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.shenouda.stride.common.domain.model.Role

@Composable
fun RoleSelectionScreen(
    viewModel: AuthViewModel,
    onGetStartedButtonClicked: (Role) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    var selectedRole by remember { mutableStateOf<Role?>(null) }

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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Center,
        ) {
            AuthHeader(
                title = "One more step",
                subtitle = "How will you use Stride?",
            )

            Spacer(Modifier.height(36.dp))

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

            Spacer(Modifier.height(36.dp))

            PrimaryGradientButton(
                text = "Get started",
                enabled = selectedRole != null && !uiState.isLoading,
                loading = uiState.isLoading,
                onClick = {
                    selectedRole?.let {
                        viewModel.updateRoleSelection(it)
                        onGetStartedButtonClicked(it)
                    }
                },
            )
        }
    }
}
