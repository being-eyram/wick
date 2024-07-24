package com.sunniercherries

import okio.FileSystem
import okio.Path
import okio.Path.Companion.toOkioPath
import kotlin.io.path.*

val FILE_SYSTEM = FileSystem.SYSTEM

val CURRENT_WORKING_DIR = Path("").toOkioPath()

val GIT_PATH = CURRENT_WORKING_DIR.resolve(".git")

val OBJECTS_PATH = GIT_PATH.resolve("objects")

val HEAD_FILE_PATH = GIT_PATH.resolve("HEAD")

val INDEX_FILE_PATH = GIT_PATH.resolve("index")

val FILES_TO_IGNORE = listOf(".", "..", ".git")

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


fun writeFile(path: Path, data: ByteArray) {
    FILE_SYSTEM.write(path) {
        write(data)
    }
}

//TODO: HANDLE ERRORS
fun getFilePaths(root: Path) = FILE_SYSTEM.list(root).filter {
    it.name !in FILES_TO_IGNORE
}

fun readHead() = readFile(HEAD_FILE_PATH)


fun updateHead(commitHash: String) {
    writeFile(HEAD_FILE_PATH, commitHash)
}
