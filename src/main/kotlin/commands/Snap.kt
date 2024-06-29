package com.sunniercherries.commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.option
import com.sunniercherries.models.*
import com.sunniercherries.models.WorkSpace.HEAD_FILE_PATH
import com.sunniercherries.readFile
import com.sunniercherries.writeFile
import kotlin.io.path.isExecutable


class Snap : CliktCommand(
    help = "Takes a shot of your project's current state."
) {

    private val message by option(names = arrayOf("--message", "-m"), help = "yelp!")

    override fun run() {
        val entries = WorkSpace.getFilePaths()?.map { path ->

            val isExecutable = path.toNioPath().isExecutable()

            val data = readFile(path) ?: return
            val blob = Blob(data)
            Database.store(blob)

            Entry(path.name, blob.hash, isExecutable)
        }

        if (entries == null) return

        val tree = Tree(entries)
        Database.store(tree)

        val author = Author(name = "Eyram Hlorgbe", email = "eyram.hlorgbe@hubtel.com")
        val parent = WorkSpace.readHead()

        val commit = Commit(parent, tree.hash, author, message ?: "")
        Database.store(commit)
        WorkSpace.updateHead(commit.hash)

        writeFile(HEAD_FILE_PATH, commit.hash)
    }
}