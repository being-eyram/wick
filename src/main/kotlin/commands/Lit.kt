package com.sunniercherries.commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.path
import java.nio.file.Files

class Lit : CliktCommand(
    help = "Initialize a wick repository"
) {

    private val destination by argument().path(
        canBeFile = false,
    )

    private val verbose by option("-v", "--verbose").flag()

    override fun run() {
        val initPath = destination.toRealPath()
        val gitPath = initPath.resolve(".git")
        if (verbose) echo("Initializing empty Wick repository")

        runCatching {
            listOf("objects", "refs").forEach {
                val gitSubDir = gitPath.resolve(it)
                Files.createDirectories(gitSubDir).also { subDir->
                    echo("Successfully created $subDir sub directory")
                }
            }
        }.onSuccess {
            if (verbose) {
                echo("Successfully initialized empty Wick repository in $gitPath")
            }
        }.onFailure {
            echo(it)
        }
    }
}