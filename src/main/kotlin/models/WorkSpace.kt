package com.sunniercherries.models

import com.sunniercherries.FILE_SYSTEM
import okio.Path
import okio.Path.Companion.toOkioPath
import java.io.File


object WorkSpace {

    val CURRENT_WORKING_DIR = File("").absoluteFile.toOkioPath()
    val GIT_PATH = CURRENT_WORKING_DIR.resolve(".git")
    val OBJECTS_PATH = GIT_PATH.resolve("objects")
    val HEAD_FILE_PATH = GIT_PATH.resolve("HEAD")

    private val filesToIgnore = listOf(".", "..", ".git")

    fun getFilePaths(): List<Path>? {
        return FILE_SYSTEM.listOrNull(CURRENT_WORKING_DIR)
            ?.filter { it.name !in filesToIgnore }
    }
}