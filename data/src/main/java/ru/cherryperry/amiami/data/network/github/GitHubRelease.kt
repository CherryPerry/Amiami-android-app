package ru.cherryperry.amiami.data.network.github

import com.google.gson.annotations.SerializedName

data class GitHubRelease(
    @SerializedName("tag_name") val tagName: String,
    @SerializedName("name") val name: String,
    @SerializedName("assets") val assets: List<GitHubAsset>
)
