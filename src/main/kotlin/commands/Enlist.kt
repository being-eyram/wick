package com.sunniercherries.commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.check
import com.github.ajalt.clikt.parameters.arguments.multiple
import com.github.ajalt.clikt.parameters.arguments.validate
import com.github.ajalt.clikt.parameters.types.path
import com.sunniercherries.*
import com.sunniercherries.models.*
import okio.ByteString.Companion.decodeHex
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

        destination.sortedBy { it.pathString }
            .forEach { path ->
                val fileAttributes = path.readAttributes<BasicFileAttributes>()
                val data = readFile(path.toOkioPath()) ?: return

                val blob = Blob(data)
                Database.store(blob)

                index.add(path, blob.hash.decodeHex(), fileAttributes)
                index.writeToIndexFile()
            }
    }
}
