package com.shenouda.stride.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.shenouda.stride.ui.theme.StrideTheme

private val TextFieldShape = RoundedCornerShape(12.dp)

@Composable
fun StrideTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    isError: Boolean = false,
    errorMessage: String = "",
    enabled: Boolean = true,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = true,
) {
    val colors = MaterialTheme.colorScheme

    Column(modifier = modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = {
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            placeholder = if (placeholder.isNotEmpty()) {
                { Text(text = placeholder, style = MaterialTheme.typography.bodyMedium) }
            } else null,
            isError = isError,
            enabled = enabled,
            singleLine = singleLine,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            shape = TextFieldShape,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = colors.primary,
                unfocusedBorderColor = colors.outline,
                errorBorderColor = colors.error,
                focusedLabelColor = colors.primary,
                unfocusedLabelColor = colors.outline,
                errorLabelColor = colors.error,
                cursorColor = colors.primary,
                focusedTextColor = colors.onSurface,
                unfocusedTextColor = colors.onSurface,
                disabledBorderColor = colors.outline.copy(alpha = 0.4f),
                disabledLabelColor = colors.outline.copy(alpha = 0.4f),
                disabledTextColor = colors.onSurface.copy(alpha = 0.4f)
            ),
            modifier = Modifier.fillMaxWidth()
        )

        if (isError && errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = MaterialTheme.typography.bodyMedium.fontSize * 0.85f),
                color = colors.error,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF7FAF6)
@Composable
private fun TextFieldPreview() {
    StrideTheme {
        var normal by remember { mutableStateOf("") }
        var filled by remember { mutableStateOf("john@example.com") }
        var error by remember { mutableStateOf("badpass") }
        var disabled by remember { mutableStateOf("Can't touch this") }

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(16.dp)
        ) {
            StrideTextField(
                value = normal,
                onValueChange = { normal = it },
                label = "Email",
                placeholder = "Enter your email"
            )
            StrideTextField(
                value = filled,
                onValueChange = { filled = it },
                label = "Email"
            )
            StrideTextField(
                value = error,
                onValueChange = { error = it },
                label = "Password",
                isError = true,
                errorMessage = "Password must be at least 8 characters"
            )
            StrideTextField(
                value = disabled,
                onValueChange = { disabled = it },
                label = "Username",
                enabled = false
            )
        }
    }
}