package com.app.android_app.data.model

import kotlinx.serialization.Serializable

@Serializable
data class DownloadResponse(
    val success: Boolean,
    val expired: Boolean
)