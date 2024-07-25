package com.sunniercherries.commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.types.path
import com.sunniercherries.*
import com.sunniercherries.models.*
import okio.ByteString.Companion.decodeHex
import okio.Path.Companion.toOkioPath
import java.nio.file.attribute.BasicFileAttributes
import kotlin.io.path.readAttributes

class Enlist : CliktCommand(
    help = "todo write help..."
) {

    private val destination by argument().path(mustExist = true)

    override fun run() {
        val fileAttributes = destination.readAttributes<BasicFileAttributes>()
        val data = readFile(destination.toOkioPath()) ?: return

        val blob = Blob(data)
        Database.store(blob)

        Index().apply {
            add(destination, blob.hash.decodeHex(), fileAttributes)
            writeToIndexFile()
        }
    }

}
