package com.bangkit.intermediate.ceritaku.source.response

import com.google.gson.annotations.SerializedName

data class AddStoryResponse(
    @SerializedName("error")
    var error: Boolean?,
    @SerializedName("message")
    var message: String?
)