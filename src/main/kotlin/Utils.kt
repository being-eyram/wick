package com.sunniercherries

import okio.FileSystem
import okio.Path

val FILE_SYSTEM = FileSystem.SYSTEM

fun readFile(path: Path): String {
    return FILE_SYSTEM.read(path) {
        readUtf8()
    }
}

fun writeFile(path: Path, data: String) {
    FILE_SYSTEM.write(path) {
        writeUtf8(data)
    }
}