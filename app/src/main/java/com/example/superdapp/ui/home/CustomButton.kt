package com.example.superdapp.ui.home

import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Text
import androidx.compose.material3.ListItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CustomButton(
    text: String,
    enabled: Boolean = true,
    colors: ButtonColors = ButtonDefaults.buttonColors(
        backgroundColor = Color.Blue,
        contentColor = Color.White,
        disabledBackgroundColor = ListItemDefaults.contentColor.copy(alpha = ContentAlpha.disabled),
        disabledContentColor = ListItemDefaults.contentColor.copy(alpha = ContentAlpha.disabled),
    ),
    onClick: () -> Unit
) {

    Button(
        onClick = onClick,
        enabled = enabled,
        colors = colors,
        modifier = Modifier.size(width = 270.dp, height = 46.dp)
    ) {
        Text(
            text = text,
            fontSize = 16.sp
        )
    }
}