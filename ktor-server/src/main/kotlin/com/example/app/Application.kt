package com.example.app

import com.example.app.plugins.configureSerialization
import com.example.app.plugins.configureRouting
import io.ktor.server.application.*
import io.ktor.server.netty.EngineMain
import kotlinx.coroutines.launch
import java.io.File

fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.module() {
    File("uploads").mkdirs()
    File("uploads/data").mkdirs()
    configureRouting()
    configureSerialization()

    launch{
        println("Starting application")
       //cleanup()
    }
}
