package com.example.app.services

import com.example.app.models.FileData
import java.io.File
import java.util.UUID

object FileService {
    private val directory = File("uploads")

    fun generateFile(id: String): File = File(directory, id)
    fun generateId(): String = UUID.randomUUID().toString()
    //Point d'amelioration: rendre l'ID plus user friendly

    fun deleteFile(id: String) = File(directory, id).deleteRecursively()
}
