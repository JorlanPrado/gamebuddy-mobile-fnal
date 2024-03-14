package com.mita.gamebuddymobile.api

import com.google.gson.annotations.SerializedName

data class InterestsRequest(
    @SerializedName("interests")
    val interests: List<Interest>
)