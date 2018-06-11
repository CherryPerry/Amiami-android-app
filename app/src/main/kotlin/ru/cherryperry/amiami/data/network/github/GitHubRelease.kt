package ru.cherryperry.amiami.data.network.github

import com.google.gson.annotations.SerializedName

class GitHubRelease {
    @SerializedName("tag_name")
    val tagName: String? = null

    @SerializedName("name")
    val name: String? = null

    @SerializedName("assets")
    val assets: Collection<GitHubAsset>? = null
}


