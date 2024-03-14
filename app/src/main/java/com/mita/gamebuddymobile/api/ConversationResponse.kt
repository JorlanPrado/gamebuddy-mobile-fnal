package com.mita.gamebuddymobile.api

import com.google.gson.annotations.SerializedName

data class ConversationResponse(
    @SerializedName("conversation_id")
    val conversationId: String,
    val conversation: Conversation,
    val message: String,
    val error: String
)