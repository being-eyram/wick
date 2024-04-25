package com.sunniercherries.commands

import com.github.ajalt.clikt.core.NoOpCliktCommand
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option

class Wick : NoOpCliktCommand(
    help = "This is a default message that comes with the command",
    epilog = "This is an epilog",
    hidden = true,
    printHelpOnEmptyArgs = true,
    helpTags = mapOf("extra" to "info")
) {
    /**
     * This overrides the default help and it's descriptilon
     */
    val help by option(names = arrayOf("--help", "-h"), help = "yelp!").flag()


    override fun run() {
        super.run()
        if (help) echoFormattedHelp()
    }
}
