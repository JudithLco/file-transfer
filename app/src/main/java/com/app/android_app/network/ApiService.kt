package com.app.android_app.network

import com.app.android_app.data.model.FileListResponse
import com.app.android_app.data.model.FileResponse
import com.app.android_app.data.model.FileUploadRequest
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.statement.HttpResponse
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import java.io.File
import io.ktor.client.request.*
import com.app.android_app.data.model.DownloadResponse
import io.ktor.client.plugins.onDownload
import io.ktor.client.statement.readBytes
import io.ktor.http.HttpStatusCode

class ApiService (private val client: HttpClient){
    private val baseUrl = "http://192.168.1.82:8080"

    suspend fun uploadFile(file: File, request: FileUploadRequest): FileResponse {
        //TODO : CHIFFREMENT AES
        //val baseUrl = context.getString(R.string.BASE_URL)
        val response: HttpResponse = client.submitFormWithBinaryData(
            url = "$baseUrl/upload",
            formData = formData {
                append("file", file.readBytes(), Headers.build {
                    append(HttpHeaders.ContentDisposition, "filename=\"${file.name}\"")
                })
                append("delete-after-use", request.deleteAfterUse.toString())
                append("expiration", request.expiration.toString())
            }
        )
        return response.body()
    }

    suspend fun fetchAllFiles(): HttpResponse {
        return client.get("$baseUrl/listAll")
    }
    
    suspend fun downloadFile(
        fileId: String,
        onProgress: (Float) -> Unit = {}
    ): Pair<FileResponse, ByteArray> {
        val response: HttpResponse = client.get("$baseUrl/download/$fileId") {
            onDownload { bytesSentTotal, contentLength ->
                if (contentLength != null) {
                    onProgress(bytesSentTotal.toFloat() / contentLength.toFloat())
                }
            }
        }

        val fileResponse = response.headers["Content-Disposition"]?.let {
            val filename = it.substringAfter("filename=").removeSurrounding("\"")
            FileResponse(
                id = fileId,
                name = filename,
                hash = "",
                expirationTime = "",
                deleteAfterUse = false,
                createdAt = ""
            )
        } ?: throw Exception("Invalid file response")

        return Pair(fileResponse, response.body())
    }
}
