package com.example.app.models

import kotlinx.serialization.Serializable
import java.time.LocalDateTime
import kotlin.time.Instant

@Serializable
data class FileData(
    val id: String,
    val name: String,
    val path: String,
    val contentType: String,
    val hash: String,
    val size: Long,
    val deleteAfterUse: Boolean,
    val expirationTime: String,
    val createdAt: String = LocalDateTime.now().toString(),
)

@Serializable
data class FileResponse(
    val id: String,
    val name: String,
    val hash: String,
    val expirationTime: String,
    val deleteAfterUse: Boolean,
    val createdAt: String,
)

@Serializable
data class ClientFileData(
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
    val files: List<ClientFileData>
)
