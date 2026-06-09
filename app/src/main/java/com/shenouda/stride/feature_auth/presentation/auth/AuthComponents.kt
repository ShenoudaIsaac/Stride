package com.shenouda.stride.feature_auth.presentation.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shenouda.stride.R
import com.shenouda.stride.ui.theme.PrimaryGradient

// ── Brand header ────────────────────────────────────────────────────────────

@Composable
fun AuthHeader(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    showLogo: Boolean = true,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (showLogo) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(PrimaryGradient),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_logo),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(40.dp),
                )
            }
            Spacer(Modifier.height(20.dp))
        }
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
    }
}

// ── Text fields ─────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    leadingIcon: Painter? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = true,
        shape = RoundedCornerShape(14.dp),
        leadingIcon = leadingIcon?.let {
            {
                Icon(
                    painter = it,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
        ),
        modifier = modifier.fillMaxWidth(),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isVisible: Boolean,
    onToggleVisibility: () -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = true,
        shape = RoundedCornerShape(14.dp),
        leadingIcon = {
            Icon(
                painter = painterResource(R.drawable.ic_lock),
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        },
        trailingIcon = {
            IconButton(onClick = onToggleVisibility) {
                Icon(
                    painter = painterResource(
                        if (isVisible) R.drawable.ic_visibility_off
                        else R.drawable.ic_visibility,
                    ),
                    contentDescription = if (isVisible) "Hide password" else "Show password",
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        },
        visualTransformation = if (isVisible) VisualTransformation.None
        else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
        ),
        modifier = modifier.fillMaxWidth(),
    )
}

// ── Buttons ─────────────────────────────────────────────────────────────────

@Composable
fun PrimaryGradientButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false,
) {
    val active = enabled && !loading
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(54.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(
                if (active) PrimaryGradient
                else SolidColor(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)),
            )
            .clickable(enabled = active) { onClick() },
        contentAlignment = Alignment.Center,
    ) {
        if (loading) {
            CircularProgressIndicator(
                color = Color.White,
                strokeWidth = 2.dp,
                modifier = Modifier.size(22.dp),
            )
        } else {
            Text(
                text = text,
                color = if (active) Color.White
                else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
            )
        }
    }
}

@Composable
fun GoogleButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    OutlinedButton(
        modifier = modifier
            .fillMaxWidth()
            .height(54.dp),
        enabled = enabled,
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)),
        onClick = onClick,
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_google),
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = Color.Unspecified,
        )
        Spacer(Modifier.size(12.dp))
        Text(
            text = text,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Medium,
        )
    }
}

// ── "or" divider ────────────────────────────────────────────────────────────

@Composable
fun OrDivider(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        HorizontalDivider(
            modifier = Modifier.weight(1f),
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
        )
        Text(
            text = "  or  ",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        HorizontalDivider(
            modifier = Modifier.weight(1f),
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
        )
    }
}

// ── Role card ───────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoleCard(
    icon: String,
    title: String,
    subtitle: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(
            width = if (selected) 2.dp else 1.dp,
            color = if (selected) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
        ),
        colors = CardDefaults.cardColors(
            containerColor = if (selected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.18f)
            else MaterialTheme.colorScheme.surface,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (selected) 0.dp else 1.dp),
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.25f)),
                contentAlignment = Alignment.Center,
            ) {
                Text(icon, fontSize = 24.sp)
            }
            Column(Modifier.weight(1f)) {
                Text(
                    title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}