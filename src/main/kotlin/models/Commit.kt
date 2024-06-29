package com.sunniercherries.models

import okio.Buffer

data class Commit(
    val parent: String,
    val tree: String,
    val author: Author,
    val message: String,
) : Snappable {

    override val type: String
        get() = "commit"

    override val payload: ByteArray
        get() {

            val body = buildString {
                appendLine("tree $tree")
                if (parent.isNotBlank()) appendLine("parent $parent")
                appendLine("author $author")
                appendLine("committer $author")
                appendLine()
                appendLine(message)
            }

            val metadata = "commit ${body.length}\u0000"

            return Buffer()
                .writeUtf8(metadata)
                .writeUtf8(body)
                .readByteArray()
        }
}
