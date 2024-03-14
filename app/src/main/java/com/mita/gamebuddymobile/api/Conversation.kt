package com.mita.gamebuddymobile.api

import com.google.gson.annotations.SerializedName

data class Conversation(
    val id: Int,
    @SerializedName("sender_id")
    val senderId: Int,
    @SerializedName("receiver_id")
    val receiverId: Int,
)