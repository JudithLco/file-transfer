package com.example.app.services

import com.example.app.models.ClientFileData
import com.example.app.models.FileData
import com.example.app.services.FileService.toClient
import kotlinx.serialization.json.Json
import java.io.File

object InfoService {
    private val directory = File("uploads/data")

    fun saveInfo(fileData: FileData): FileData {
        val file = File(directory, "${fileData.id}.info.json")
        file.writeText(Json.encodeToString(fileData))
        return fileData
    }

    fun read(id: String): FileData? {
        val file = File(directory, "${id}.info.json")
        return if (file.exists()){
            Json.decodeFromString<FileData>(file.readText())
        } else   {
            null
        }
    }

    fun listAllData(): List<ClientFileData> {

        return directory.listFiles()
            ?.filter {it.name.endsWith(".info.json")}
            ?.mapNotNull { file ->
                try {
                    Json.decodeFromString<FileData>(file.readText()).toClient()
                } catch (e: Exception){
                    null
                }
            } ?: emptyList()
    }
}