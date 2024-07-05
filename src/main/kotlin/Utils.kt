package com.sunniercherries

import okio.FileSystem
import okio.Path.Companion.toOkioPath
import java.nio.file.Path
import kotlin.io.path.*

val FILE_SYSTEM = FileSystem.SYSTEM
val CURRENT_WORKING_DIR = Path("")
val GIT_PATH = CURRENT_WORKING_DIR.resolve(".git")
val OBJECTS_PATH = GIT_PATH.resolve("objects")
val HEAD_FILE_PATH = GIT_PATH.resolve("HEAD")

val FILES_TO_IGNORE = listOf(".", "..", ".git")

fun readFile(path: Path): String? {
    val result = runCatching {
        FILE_SYSTEM.read(path.toOkioPath()) {
            readUtf8()
        }
    }

    return result.getOrNull()
}

fun writeFile(path: Path, data: String) {
    FILE_SYSTEM.write(path.toOkioPath()) {
        writeUtf8(data)
    }
}


fun writeFile(path: Path, data: ByteArray) {
    FILE_SYSTEM.write(path.toOkioPath()) {
        write(data)
    }
}

fun getFilePaths(root: Path) = root.listDirectoryEntries().filter {
    !it.pathString.startsWith(".")
}

fun readHead() = readFile(HEAD_FILE_PATH)


fun updateHead(commitHash: String) {
    writeFile(HEAD_FILE_PATH, commitHash)
}