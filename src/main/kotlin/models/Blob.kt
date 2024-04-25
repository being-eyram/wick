package com.sunniercherries.models

import okio.Buffer
import okio.ByteString

data class Blob(val data: ByteString) {
    val oid: String
        get() = computeObjectId()

    private val type: String = "blob"

    val commitDescriptor: String
        get() = "$type ${data.size}\u0000${data.string(Charsets.US_ASCII)}"

    private fun computeObjectId(): String {
        return Buffer().writeUtf8(commitDescriptor)
            .sha1()
            .hex()
    }
}