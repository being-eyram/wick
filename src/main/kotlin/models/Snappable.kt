package com.sunniercherries.models

import okio.Buffer

interface Snappable {

    val hash: String

    val type: String

    val payload: ByteArray

    fun computeHash(): String {
        return Buffer().write(payload)
            .sha1()
            .hex()
    }
}