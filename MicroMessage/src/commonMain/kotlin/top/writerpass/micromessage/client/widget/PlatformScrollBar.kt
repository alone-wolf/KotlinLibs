package top.writerpass.micromessage.client.widget

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable

@Composable
expect fun BoxScope.PlatformScrollBar(
    lazyListState: LazyListState
)