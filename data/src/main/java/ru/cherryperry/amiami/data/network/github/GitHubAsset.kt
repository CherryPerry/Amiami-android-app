package ru.cherryperry.amiami.data.network.github

import com.google.gson.annotations.SerializedName

data class GitHubAsset(
    @SerializedName("browser_download_url") val url: String
)
