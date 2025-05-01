package com.app.android_app.data.model

import kotlinx.serialization.Serializable

@Serializable
data class FileResponse(
    val id: String,
    val name: String,
    val hash: String,
    val expirationTime: String,
    val deleteAfterUse: Boolean,
    val createdAt: String
)