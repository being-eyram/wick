package com.sunniercherries.models

import okio.ByteString
import okio.FileSystem
import okio.Path
import okio.Path.Companion.toOkioPath
import java.io.File
import java.nio.file.Paths


object WorkSpace {
    private val fileSystem = FileSystem.SYSTEM
    private val filesToIgnore = listOf(".", "..", ".git")

    val currentWorkingDir = File("").absoluteFile.toOkioPath()
    val gitPath = Paths.get(currentWorkingDir.toString(), ".git")
    val objectsPath = Paths.get(gitPath.toString(), "objects")

    fun getFilePaths(): List<Path>? {
        return fileSystem.listOrNull(currentWorkingDir)
            ?.filter { it.name !in filesToIgnore }
    }

    fun readFile(path: Path): ByteString {
        return fileSystem.read(path) {
            readByteString()
        }
    }
}