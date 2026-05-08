package com.shenouda.stride.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.shenouda.stride.ui.theme.StrideTheme
import com.shenouda.stride.ui.theme.PrimaryGradient

private val ButtonShape = RoundedCornerShape(100.dp)
private val ButtonHeight = 56.dp

@Composable
fun StridePrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val interactionSource = remember { MutableInteractionSource() }
    val alpha = if (enabled) 1f else 0.4f

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxWidth()
            .height(ButtonHeight)
            .clip(ButtonShape)
            .background(brush = PrimaryGradient, alpha = alpha)
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(color = Color.White),
                enabled = enabled,
                onClick = onClick
            )
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = alpha)
        )
    }
}

@Composable
fun StrideSecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val interactionSource = remember { MutableInteractionSource() }
    val primaryColor = MaterialTheme.colorScheme.primary
    val alpha = if (enabled) 1f else 0.4f

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxWidth()
            .height(ButtonHeight)
            .clip(ButtonShape)
            .border(
                width = 1.5.dp,
                color = primaryColor.copy(alpha = alpha),
                shape = ButtonShape
            )
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(color = primaryColor),
                enabled = enabled,
                onClick = onClick
            )
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = primaryColor.copy(alpha = alpha)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF7FAF6)
@Composable
private fun ButtonPreview() {
    StrideTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StridePrimaryButton(text = "Get Started", onClick = {})
            StridePrimaryButton(text = "Disabled", onClick = {}, enabled = false)
            StrideSecondaryButton(text = "Log In", onClick = {})
            StrideSecondaryButton(text = "Disabled", onClick = {}, enabled = false)
        }
    }
}