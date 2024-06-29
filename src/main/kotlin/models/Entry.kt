package com.sunniercherries.models

data class Entry(
    val name: String,
    val hash: String,
    val isExecutable: Boolean = false
) {
    companion object {
        private const val REGULAR_MODE = "100644"
        private const val EXECUTABLE_MODE = "100755"
    }

    val mode: String by lazy {
        if (isExecutable) EXECUTABLE_MODE else REGULAR_MODE
    }
}