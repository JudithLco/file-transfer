package com.example.app.services

import com.example.app.models.ClientFileData
import com.example.app.models.FileData
import java.io.File
import java.util.UUID

object FileService {
    private val directory = File("uploads")
    private val dataDirectory = File(directory, "data")

    fun generateFile(id: String): File = File(directory, id)
    fun generateId(): String = UUID.randomUUID().toString()
    //Point d'amelioration: rendre l'ID plus user friendly

    fun deleteFile(id: String) {
        File(directory, id).deleteRecursively()
        File(dataDirectory, "${id}.info.json").deleteRecursively()
    }

    fun FileData.toClient(): ClientFileData = ClientFileData(
        id, name, contentType, hash, size, deleteAfterUse, expirationTime, createdAt
    )
}
