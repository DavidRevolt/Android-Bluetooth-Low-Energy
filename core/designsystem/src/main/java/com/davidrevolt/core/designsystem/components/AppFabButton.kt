package com.davidrevolt.core.designsystem.components

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun AppFabButton(onFabClick: () -> Unit, icon: ImageVector, modifier: Modifier = Modifier, containerColor: Color = FloatingActionButtonDefaults.containerColor) {
    FloatingActionButton(
        onClick = onFabClick,
        modifier = modifier,
        containerColor = containerColor,
        shape = CircleShape,
    ) {
        Icon(icon, "Floating action button")
    }
}