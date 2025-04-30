package com.example.githubclient.data.model

import com.google.gson.annotations.SerializedName

data class GithubEvent(
    val id: String,
    val type: String,
    val repo: Repo,
    val payload: Payload,
    @SerializedName("created_at") val createdAt: String
)

data class Repo(
    val name: String
)

data class Payload(
    val action: String?,
    val release: Release?
)

data class Release(
    val name: String?,
    @SerializedName("tag_name") val tagName: String?,
    @SerializedName("html_url") val htmlUrl: String,
    val body: String?
)