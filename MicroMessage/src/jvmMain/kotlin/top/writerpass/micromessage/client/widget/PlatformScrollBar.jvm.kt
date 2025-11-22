package top.writerpass.micromessage.client.widget

import androidx.compose.foundation.LocalScrollbarStyle
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
actual fun BoxScope.PlatformScrollBar(lazyListState: LazyListState) {
    val style = LocalScrollbarStyle.current
    val newStyle = style.copy(
        shape = RoundedCornerShape(4.dp)
    )
    VerticalScrollbar(
        adapter = rememberScrollbarAdapter(scrollState = lazyListState),
        modifier = Modifier,
        reverseLayout = false,
        style = newStyle
    )
}