package com.mita.gamebuddymobile.api

import com.google.gson.annotations.SerializedName

data class Interest(
    @SerializedName("game")
    val game: String
)
