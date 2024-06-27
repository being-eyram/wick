package com.sunniercherries.models

import okio.FileSystem
import okio.Path
import okio.Path.Companion.toOkioPath
import java.io.File
import java.nio.file.Paths


object WorkSpace {
    val FILE_SYSTEM = FileSystem.SYSTEM

    val CURRENT_WORKING_DIR = File("").absoluteFile.toOkioPath()
    val GIT_PATH = CURRENT_WORKING_DIR.resolve(".git")
    val OBJECTS_PATH = GIT_PATH.resolve("objects")
    val HEAD_FILE = GIT_PATH.resolve("HEAD")

    private val filesToIgnore = listOf(".", "..", ".git")

    fun getFilePaths(): List<Path>? {
        return FILE_SYSTEM.listOrNull(CURRENT_WORKING_DIR)
            ?.filter { it.name !in filesToIgnore }
    }

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
}