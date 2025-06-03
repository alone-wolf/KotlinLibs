package io.github.kotlin.fibonacci.compose.widget

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay
import utils.Mutable
import java.text.BreakIterator
import java.text.StringCharacterIterator

@Composable
private fun AnimatedText(text: String) {
    // Use BreakIterator as it correctly iterates over characters regardless of how they are
    // stored, for example, some emojis are made up of multiple characters.
    // You don't want to break up an emoji as it animates, so using BreakIterator will ensure
    // this is correctly handled!
    val breakIterator = remember(text) { BreakIterator.getCharacterInstance() }

    // Define how many milliseconds between each character should pause for. This will create the
    // illusion of an animation, as we delay the job after each character is iterated on.
    val typingDelayInMs = 50L

    var substringText by Mutable.Something("")

//                var substringText by remember { mutableStateOf("") }
    LaunchedEffect(text) {
        // Initial start delay of the typing animation
        delay(1000)
        breakIterator.text = StringCharacterIterator(text)

        var nextIndex = breakIterator.next()
        // Iterate over the string, by index boundary
        while (nextIndex != BreakIterator.DONE) {
            substringText = text.subSequence(0, nextIndex).toString()
            // Go to the next logical character boundary
            nextIndex = breakIterator.next()
            delay(typingDelayInMs)
        }
    }
    Text(substringText)
}


@Composable
fun AAA() {
    val textList = remember { mutableStateListOf<String>() }

    LaunchedEffect(Unit) {
        textList += "这段代码会逐字符为文本添加动画效果。"
        delay(1000L)
        textList += "它会跟踪一个索引，以控制显示的文本量。"
        delay(1000L)
        textList += "显示的文本会动态更新，以仅显示当前索引之前的字符。"
        delay(1000L)
        textList += "最后，该变量会在发生变化时运行动画。"
        delay(1000L)
        textList += "https://developer.android.com/develop/ui/compose/quick-guides/content/animate-text?hl=zh-cn"
    }

    Column {
        textList.forEach {
            AnimatedText(it)
        }
    }
}