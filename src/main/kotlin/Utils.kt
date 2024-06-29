package com.sunniercherries

import okio.FileSystem
import okio.Path

val FILE_SYSTEM = FileSystem.SYSTEM

fun readFile(path: Path): String? {
    val result = runCatching {
        FILE_SYSTEM.read(path) {
            readUtf8()
        }
    }

    return result.getOrNull()
}

fun writeFile(path: Path, data: String) {
    FILE_SYSTEM.write(path) {
        writeUtf8(data)
    }
}