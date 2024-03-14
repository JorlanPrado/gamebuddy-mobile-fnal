package com.mita.gamebuddymobile.api

import com.google.gson.annotations.SerializedName

data class ReportRequest(
    @SerializedName("name") val name: String,
    @SerializedName("reason") val reason: String
)
