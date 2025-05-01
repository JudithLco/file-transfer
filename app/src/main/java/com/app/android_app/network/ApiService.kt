package com.app.android_app.network

import com.app.android_app.data.model.FileListResponse
import com.app.android_app.data.model.FileResponse
import com.app.android_app.data.model.FileUploadRequest
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import java.io.File
import io.ktor.client.request.*
import android.content.Context
import com.app.android_app.R

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
    }
