package ru.cherryperry.amiami.data.network.server

import com.google.gson.annotations.SerializedName

data class RemoteItem(
    @SerializedName("url") val url: String,
    @SerializedName("image") val image: String,
    @SerializedName("name") val name: String,
    @SerializedName("discount") val discount: String,
    @SerializedName("price") val price: String,
    @SerializedName("time") val time: Long = 0
)
