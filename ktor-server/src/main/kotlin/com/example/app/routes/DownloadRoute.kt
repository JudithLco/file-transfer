package com.example.app.routes
import com.example.app.services.FileService
import com.example.app.services.InfoService
import io.ktor.http.*
import io.ktor.server.routing.*
import io.ktor.server.response.*
import java.io.File
import kotlin.time.*
import kotlin.time.Clock.System.now

@OptIn(ExperimentalTime::class)
fun Route.downloadRoute(){
    get("/download/{id}"){
        val id = call.parameters["id"] ?: return@get call.respond(HttpStatusCode.BadRequest,"No ID")
        val data = InfoService.read(id) ?: return@get call.respond(HttpStatusCode.NotFound, "Found no file corresponding to that ID")
        val expiration = Instant.parse(data.expirationTime)
        val file = File(data.path)

        if (now() > expiration) {
            FileService.deleteFile(id)
            return@get call.respond(HttpStatusCode.Gone, "File has been deleted because it was expired")
        }

        if (!File(data.path).exists()) {
            return@get call.respond(HttpStatusCode.NotFound, "File does not exist")
        }

        call.response.header(
            HttpHeaders.ContentDisposition,
            ContentDisposition.Attachment.withParameter(ContentDisposition.Parameters.FileName, data.name)
                .toString()
        )
        call.respondFile(file)

        if (data.deleteAfterUse) {
            FileService.deleteFile(id)
        }

    }
}