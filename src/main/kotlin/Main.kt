package com.sunniercherries

import com.github.ajalt.clikt.core.*
import com.sunniercherries.commands.Lit
import com.sunniercherries.commands.Snap
import com.sunniercherries.commands.Wick


fun main(args: Array<String>) = Wick()
    .subcommands(Lit(), Snap())
    .main(args)
