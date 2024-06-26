package com.sunniercherries.models

import okio.Buffer
import okio.ByteString.Companion.decodeHex
import okio.ByteString.Companion.encode

data class Tree(
    val entries: List<Entry>
) : Snappable {

    companion object {
        val MODE = "100644"
    }

    override val type: String
        get() = "tree"

    override val payload: ByteArray
        get() {

            val body = Buffer().run {
                val sortedEntries = entries.sortedBy { it.name }

                sortedEntries.forEach { entry ->
                    val entryMetadata = "$MODE ${entry.name}\u0000".encode()
                    val entryHash = entry.hash.decodeHex()

                    write(entryMetadata)
                    write(entryHash)
                }

                readByteString()
            }

            val metadata = "$type ${body.size}\u0000".encode()

            return Buffer()
                .write(metadata)
                .write(body)
                .readByteArray()
        }

}
