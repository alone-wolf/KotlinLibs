package top.writerpass.animates.b

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

@Composable
fun <T> FlipNavigatorList(
    items: List<T>,
    itemKey: (T) -> Any = { it!! },
    itemContent: @Composable (T) -> Unit,
    detailContent: @Composable (T, () -> Unit) -> Unit
) {
    val selectedItem = remember { mutableStateOf<T?>(null) }
    val selectedBounds = remember { mutableStateOf<Rect?>(null) }

    val density = LocalDensity.current
    val overlayVisible = selectedItem.value != null && selectedBounds.value != null

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn {
            items(items, key = itemKey) { item ->
                var itemBounds by remember { mutableStateOf<Rect?>(null) }

                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .height(100.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.LightGray)
                        .clickable {
                            selectedItem.value = item
                            selectedBounds.value = itemBounds
                        }
                        .onGloballyPositioned {
                            val bounds = it.boundsInWindow()
                            itemBounds = Rect(bounds.topLeft, bounds.size)
                        }
                ) {
                    itemContent(item)
                }
            }
        }

        if (overlayVisible) {
            FlipOverlayPage(
                startBounds = selectedBounds.value!!,
                onDismiss = {
                    selectedItem.value = null
                    selectedBounds.value = null
                },
                front = {
                    Box(modifier = Modifier.fillMaxSize()) {
                        itemContent(selectedItem.value!!)
                    }
                },
                back = { onBack ->
                    detailContent(selectedItem.value!!, onBack)
                }
            )
        }
    }
}