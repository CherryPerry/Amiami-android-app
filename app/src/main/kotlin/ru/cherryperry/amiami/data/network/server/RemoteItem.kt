package ru.cherryperry.amiami.data.network.server

import com.google.gson.annotations.SerializedName

class RemoteItem {
    @SerializedName("url")
    var url: String? = null

    @SerializedName("image")
    var image: String? = null

    @SerializedName("name")
    var name: String? = null

    @SerializedName("discount")
    var discount: String? = null

    @SerializedName("price")
    var price: String? = null

    @SerializedName("time")
    var time: Long = 0
}
