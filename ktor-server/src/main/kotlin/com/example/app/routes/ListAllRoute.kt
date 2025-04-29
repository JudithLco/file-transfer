package com.example.app.routes
import com.example.app.models.FileListResponse
import com.example.app.services.InfoService
import io.ktor.server.routing.*
import io.ktor.server.request.*
import io.ktor.server.response.respond

fun Route.listAll() {
    get("/listAll"){
        val allData = InfoService.listAllData()
        call.respond(FileListResponse(files=allData))
    }
}