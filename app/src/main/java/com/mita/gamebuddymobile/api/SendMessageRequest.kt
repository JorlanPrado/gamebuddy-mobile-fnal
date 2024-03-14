package com.mita.gamebuddymobile.api

data class SendMessageRequest(
    val body: String,
    val receiverId: Int
)

