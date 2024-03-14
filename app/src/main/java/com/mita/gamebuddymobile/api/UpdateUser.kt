package com.mita.gamebuddymobile.api

import com.google.gson.annotations.SerializedName

data class UpdateUser(
    @SerializedName("name")
    val name: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("password")
    val password: String
)
