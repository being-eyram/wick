package com.sunniercherries.models

import okio.Buffer
import okio.ByteString
import java.nio.charset.Charset

data class Tree(
    val entries: List<Entry>
) : Snappable {

    companion object {
        val MODE = "10644"
    }

    override val type: String
        get() = "tree"

    @OptIn(ExperimentalStdlibApi::class)
    override val content: String
        get() = entries
            .sortedBy { it.name }
            .map { entry ->
                buildString {
                    append("${MODE} ${entry.name}\u0000")
                    append(entry.oid.hexToByteArray())
                }
            }.joinToString("")
}
