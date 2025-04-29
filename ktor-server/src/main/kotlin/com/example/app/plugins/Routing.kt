package com.example.app.plugins

import io.ktor.server.application.*
import io.ktor.server.routing.*
import com.example.app.routes.uploadRoute
import com.example.app.routes.downloadRoute
import com.example.app.routes.listAll

fun Application.configureRouting() {

    routing {
        uploadRoute()
        downloadRoute()
        listAll()
    }
}
