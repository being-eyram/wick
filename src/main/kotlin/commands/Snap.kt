package com.sunniercherries.commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.option
import com.sunniercherries.*
import com.sunniercherries.models.*
import okio.Path

import kotlin.io.path.isDirectory
import kotlin.io.path.isExecutable



class Snap : CliktCommand(
    help = "Takes a shot of your project's current state."
) {

    private val message by option(names = arrayOf("--message", "-m"), help = "yelp!")
    override fun run() {
        fun processDirectory(root: Path): Tree {
            val entries = getFilePaths(root).map { path ->
                val ktPath = path.toNioPath()
                if (ktPath.isDirectory()) {
                    val subtree = processDirectory(path)
                    Database.store(subtree)
                    Entry(path.name, subtree.hash, isDirectory = true)
                } else {
                    val isExecutable = ktPath.isExecutable()
                    val data = readFile(path) ?: return@map null
                    val blob = Blob(data)
                    Database.store(blob)
                    Entry(path.name, blob.hash, isExecutable)
                }
            }.filterNotNull() // Remove any null entries

            val tree = Tree(entries)
            return tree
        }

        val rootTree = processDirectory(CURRENT_WORKING_DIR)
        Database.store(rootTree)

        val author = Author(name = "Eyram Hlorgbe", email = "eyram.hlorgbe@hubtel.com")
        val parent = readHead()
        val commit = Commit(parent, rootTree.hash, author, message ?: "")

        Database.store(commit)
        updateHead(commit.hash)
    }

}
