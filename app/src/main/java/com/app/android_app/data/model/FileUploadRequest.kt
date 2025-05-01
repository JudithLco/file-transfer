package com.app.android_app.data.model

import kotlinx.serialization.Serializable

@Serializable
data class FileUploadRequest(
    val deleteAfterUse : Boolean,
    val expiration: Long
)