@file:OptIn(ExperimentalTime::class)

package top.writerpass.micromessage.client.viewmodels

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

data class PrivateChatMessage(
    val content: String,
    val sentAt: Long,
    val receivedAt: Long,
    val readAt: Long,
    val isMe: Boolean
) {
    companion object {
        fun new(content: String, isMe: Boolean) = PrivateChatMessage(
            content = content,
            sentAt = Clock.System.now().toEpochMilliseconds(),
            receivedAt = Clock.System.now().toEpochMilliseconds(),
            readAt = Clock.System.now().toEpochMilliseconds(),
            isMe = isMe
        )
    }
}

class PrivateChatViewModel(
    val chatId: Long
) : ViewModel() {
    val chatMessages = mutableStateListOf(
        PrivateChatMessage.new("Hello", false),
        PrivateChatMessage.new("Long time no see", false),
        PrivateChatMessage.new("Yes", false),
        PrivateChatMessage.new("hao ru", false),
    )

    var text by mutableStateOf("")
        private set
    val textNotEmpty by derivedStateOf {
        text.isNotEmpty()
    }

    fun updateText(new: String) {
        text = new
    }

    fun sendMessage() {
        chatMessages.add(
            PrivateChatMessage.new(text, true)
        )
        text = ""
    }
}