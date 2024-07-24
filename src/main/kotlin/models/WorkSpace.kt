package com.sunniercherries.models

import com.sunniercherries.FILE_SYSTEM
import com.sunniercherries.readFile
import com.sunniercherries.writeFile
import okio.Path
import okio.Path.Companion.toOkioPath
import java.io.File


object WorkSpace {

    val CURRENT_WORKING_DIR
        get() = File("").absoluteFile.toOkioPath()

    val GIT_PATH
        get() = CURRENT_WORKING_DIR.resolve(".git")

    val OBJECTS_PATH
        get() = GIT_PATH.resolve("objects")

    val HEAD_FILE_PATH
        get() = GIT_PATH.resolve("HEAD")

    val INDEX_FILE_PATH
        get() = GIT_PATH.resolve("index")

    private val filesToIgnore = listOf(".", "..", ".git")

    fun getFilePaths(): List<Path>? {
        return FILE_SYSTEM
            .listOrNull(CURRENT_WORKING_DIR)
            ?.filter { it.name !in filesToIgnore }
    }

    fun readHead() = readFile(HEAD_FILE_PATH)

    fun updateHead(commitHash: String) {
        writeFile(HEAD_FILE_PATH, commitHash)
    }
}
