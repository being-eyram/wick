package com.sunniercherries.models

import okio.Buffer

interface Snappable {

    val oid: String
        get() = computeObjectId()

    val type: String

    val content: String

    private fun computeObjectId(): String {
        return Buffer().writeUtf8(content)
            .sha1()
            .hex()
    }

}