package com.sunniercherries.models

import okio.ByteString

data class Blob(val data: String) : Snappable {

    override val hash: String by lazy {
        computeHash()
    }

    override val type: String
        get() = "blob"

    override val payload: ByteArray
        get() = "$type ${data.length}\u0000${data}".toByteArray()

}