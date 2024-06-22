package com.sunniercherries.models

import okio.Buffer

interface Snappable {

    val hash: String
        get() = computeHash()

    val type: String

    val payload: ByteArray

    private fun computeHash(): String {
        return Buffer().write(payload)
            .sha1()
            .hex()
    }
}