package com.sunniercherries.models

import okio.Buffer
import okio.ByteString.Companion.decodeHex
import okio.ByteString.Companion.encode
import okio.use

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

            val payloadBuffer = Buffer()
            val sortedEntries = entries.sortedBy { it.name }

            sortedEntries.forEach { entry ->
                val entryMetadata = "$MODE ${entry.name}\u0000".encode()
                val entryHash = entry.hash.decodeHex()

                payloadBuffer
                    .write(entryMetadata)
                    .write(entryHash)
            }

            val metadataBuffer = Buffer()
                .write("$type ${payloadBuffer.size}\u0000".encode())


            return metadataBuffer
                .write(payloadBuffer.readByteString())
                .readByteArray()
        }

}
