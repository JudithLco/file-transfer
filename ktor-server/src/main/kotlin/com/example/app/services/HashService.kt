package com.example.app.services

import java.security.MessageDigest

object HashService {
    fun sha256(bytes: ByteArray): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hash = digest.digest(bytes)
        return hash.joinToString("") { String.format("%02x", it) }
    }

    fun validateSha256(input: ByteArray, expectedHash: String): Boolean {
        val calculatedHash = sha256(input)
        return calculatedHash.equals(expectedHash, ignoreCase = true)
    }
}
