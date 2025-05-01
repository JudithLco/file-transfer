package com.app.android_app.data.model

import kotlinx.serialization.Serializable

@Serializable
data class FileListItem(
    val id: String,
    val name: String,
    val contentType: String,
    val hash: String,
    val size: Long,
    val deleteAfterUse: Boolean,
    val expirationTime: String,
    val createdAt: String
)

@Serializable
data class FileListResponse(
    val files: List<FileListItem>
)