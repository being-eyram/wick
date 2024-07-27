package com.sunniercherries.commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.multiple
import com.github.ajalt.clikt.parameters.types.path
import com.sunniercherries.*
import com.sunniercherries.models.*
import okio.ByteString.Companion.decodeHex
import okio.Path
import okio.Path.Companion.toOkioPath
import java.nio.file.attribute.BasicFileAttributes
import kotlin.io.path.pathString
import kotlin.io.path.readAttributes

class Enlist : CliktCommand(
    help = "todo write help..."
) {

    private val destination by argument()
        .path(mustExist = true)
        .multiple()
    //Add validation logic to destination

    override fun run() {
        val index = Index()

        destination.forEach { path ->
            val okioPath = path.toOkioPath()
            when {
                okioPath.isRegularFile -> addToIndex(okioPath, index)
                else -> okioPath.listFiles().forEach {
                    addToIndex(it, index)
                }
            }
        }

        index.writeToIndexFile()
    }

    private fun addToIndex(file: Path, index: Index): Boolean {
        val fileAttributes = file.toNioPath().readAttributes<BasicFileAttributes>()
        val data = readFile(file) ?: return true

        val blob = Blob(data)
        Database.store(blob)

        index.add(file.toNioPath(), blob.hash.decodeHex(), fileAttributes)
        return false
    }
}
