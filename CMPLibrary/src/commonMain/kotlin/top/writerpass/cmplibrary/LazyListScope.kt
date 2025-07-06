package top.writerpass.cmplibrary

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.ui.Modifier

fun LazyListScope.spacer(modifier: Modifier) {
    item {
        Spacer(modifier = modifier)
    }
}