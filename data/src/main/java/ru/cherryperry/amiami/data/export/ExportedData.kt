package ru.cherryperry.amiami.data.export

import com.google.gson.annotations.SerializedName

data class ExportedData(
    @SerializedName("highlight") var highlight: List<String>
)
