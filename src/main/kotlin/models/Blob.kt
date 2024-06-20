package com.sunniercherries.models

import okio.ByteString

data class Blob(val data: String) : Snappable {
    override val type: String
        get() = "blob"

    override val content: String
        get() = "$type ${data.length}\u0000${data}"

}