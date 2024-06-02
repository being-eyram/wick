package com.sunniercherries.models

import okio.Buffer
import okio.ByteString

data class Blob(val data: ByteString) : Snappable {
    override val type: String
        get() = "blob"

    override val content: String
        get() = "$type ${data.size}\u0000${data.string(Charsets.US_ASCII)}"
}