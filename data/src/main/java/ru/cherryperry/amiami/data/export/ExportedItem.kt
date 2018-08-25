package ru.cherryperry.amiami.data.export

import com.google.gson.annotations.SerializedName

data class ExportedItem(
    @SerializedName("value") var value: String,
    @SerializedName("regex") var regex: Boolean
)
