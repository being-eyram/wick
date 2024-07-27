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
fun getFilePaths(root: Path) = FILE_SYSTEM
    .list(root)
    .filter { it.isNotGitSubDirectory }


fun readHead() = readFile(HEAD_FILE_PATH)

fun updateHead(commitHash: String) {
    writeFile(HEAD_FILE_PATH, commitHash)
}

val Path.isNotGitSubDirectory: Boolean
    get() = this.segments.all { it != ".git" }

val Path.isRegularFile: Boolean
    get() = this.toNioPath().isRegularFile()

val Path.isDirectory: Boolean
    get() = this.toNioPath().isDirectory()

val Path.isExecutable: Boolean
    get() = this.toNioPath().isExecutable()

fun Path.listFiles() = FILE_SYSTEM
    .listRecursively(this)
    .filter { it.isNotGitSubDirectory && it.isRegularFile }
    .map { it.relativeFrom(this) }

fun Path.relativeFrom(path: Path): Path {
    return this.toNioPath()
        .relativeTo(path.toNioPath())
        .toOkioPath()
}

