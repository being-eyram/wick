package com.sunniercherries.commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.terminal
import com.sunniercherries.models.Blob
import com.sunniercherries.models.Database
import com.sunniercherries.models.WorkSpace
import java.nio.file.Paths
import kotlin.system.exitProcess

class Snap : CliktCommand(
    help = "Takes a shot of your project's current state."
) {
    override fun run() {
        WorkSpace.apply {
            getFilePaths()?.forEach {
                val data = readFile(it)
                val didSucceed = Database.store(Blob(data))

                if (didSucceed) {
                    echo("successfully snapped a file")
                }
            }
        }
    }
}