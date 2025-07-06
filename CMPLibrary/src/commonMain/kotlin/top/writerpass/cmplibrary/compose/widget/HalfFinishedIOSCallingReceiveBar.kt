package top.writerpass.cmplibrary.compose.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import top.writerpass.cmplibrary.compose.Text
import top.writerpass.cmplibrary.compose.FullSizeBox

@Composable
fun HalfFinishedIOSCallingReceiveBar(){
    FullSizeBox {
        Box(
            modifier = Modifier
                .width(600.dp)
                .height(170.dp)
                .align(Alignment.Center)
                .clip(RoundedCornerShape(100.dp))
//                .hazeEffect(
//                    state = hazeStatePages(),
//                    style = HazeMaterials.ultraThin()
//                )
                .padding(horizontal = 8.dp)
        ) {
            "Swipe to start".Text(modifier = Modifier.align(Alignment.Center))
            var offsetX by remember { mutableStateOf(0f) }
            Box(
                modifier = Modifier
                    .size(154.dp)
                    .offset { IntOffset(offsetX.toInt(), 0) }
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
                    .pointerInput(Unit) {
                        detectHorizontalDragGestures { change, dragAmount ->
                            change.consume()
                            val newOffsetX = offsetX + dragAmount
                            offsetX = newOffsetX.coerceIn(0f, 446f) // 600.dp - 154.dp
                        }
                    }
            )
        }
    }
}