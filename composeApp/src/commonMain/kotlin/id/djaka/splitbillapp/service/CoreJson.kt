package id.djaka.splitbillapp.service

import kotlinx.serialization.json.Json

val coreJson = Json

inline fun <reified T> Json.safeDecodeFromString(string: String): T? {
    return try {
        decodeFromString(string)
    } catch (e: Exception) {
        println("Failed to decode string: $string")
        null
    }
}