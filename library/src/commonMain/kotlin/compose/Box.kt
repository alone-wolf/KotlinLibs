package io.github.kotlin.fibonacci.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun FullSizeBox(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) = Box(
    modifier = modifier.fillMaxSize(),
    content = content
)