package com.sunniercherries.commands

import com.github.ajalt.clikt.core.CliktCommand
import com.sunniercherries.models.*


class Snap : CliktCommand(
    help = "Takes a shot of your project's current state."
) {
    override fun run() {
        val entries = WorkSpace.getFilePaths()?.map { path ->

            //val isRegularFile = path.toNioPath().isRegularFile()
            val data = WorkSpace.readFile(path)
            val blob = Blob(data)
            Database.store(blob)
            Entry(path.name, blob.hash)
        }

        val tree = entries?.let {
            Tree(it)
        }

        val didSucceed = tree?.run {
            Database.store(this)
        }

        if (didSucceed == true) {
            echo("Successfully snapped a file")
        }
    }
}