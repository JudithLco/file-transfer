package com.example.app.routes

import com.example.app.models.FileData
import com.example.app.models.FileResponse
import com.example.app.services.FileService
import com.example.app.services.HashService
import com.example.app.services.InfoService
import io.ktor.http.HttpStatusCode
import io.ktor.server.routing.*
import io.ktor.server.request.*
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart
import io.ktor.server.response.respond
import io.ktor.util.cio.writeChannel
import io.ktor.utils.io.copyAndClose
import java.io.File
import kotlin.time.Clock.System.now
import kotlin.time.Duration.Companion.hours
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
fun Route.uploadRoute() {
    post("/upload") {

        //TODO: verifications file size, name and type for better security :3
        val multipart = call.receiveMultipart(formFieldLimit = 1024 * 1024 * 100)
        var name: String? = null
        var contentType: String? = null
        var size: Long? = null
        var deleteAfterUse: Boolean? = null
        var fileId: String? = null
        var file: File? = null
        var expiration: Long = 24

        multipart.forEachPart { part ->
            when (part) {
                is PartData.FormItem -> {
                    when (part.name) {
                        "delete-after-use" -> deleteAfterUse = part.value.toBoolean();
                        "expiration" -> expiration = part.value.toLong();
                    }

                }
                is PartData.FileItem -> {
                    name = part.originalFileName
                    contentType = part.contentType?.toString()
                    fileId = FileService.generateId()
                    file = FileService.generateFile(fileId)
                    part.provider().copyAndClose(file.writeChannel())
                    size = file.length()
                }
                else -> {}
            }
            part.dispose()
        }

        if (expiration < 1 || expiration > 24*7 ) {
            expiration = 24
        }

        if (name == null || file == null || fileId == null) {
            call.respond(HttpStatusCode.BadRequest, "Missing file")
            return@post
        }

        val data = FileData(
            id = fileId,
            name = name!!,
            contentType = contentType ?: "application/octet-stream",
            hash = HashService.sha256(file.readBytes()),
            path = file.absolutePath,
            size = size!!,
            deleteAfterUse = deleteAfterUse ?: false,
            expirationTime = now().plus(expiration.hours).toString(),
            createdAt = now().toString(),
        )

        InfoService.saveInfo(data)

        call.respond(HttpStatusCode.OK, FileResponse(
            id = data.id,
            name = data.name,
            hash = data.hash,
            expirationTime = data.expirationTime,
            deleteAfterUse = data.deleteAfterUse,
            createdAt = data.createdAt,
        ))
    }
}
