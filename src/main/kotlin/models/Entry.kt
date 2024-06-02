package com.sunniercherries.models

import okio.Path

data class Entry(
    val name: Path,
    val oid: String,
)