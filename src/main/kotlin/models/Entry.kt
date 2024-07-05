package com.sunniercherries.models

data class Entry(
    val name: String,
    val hash: String,
    val isExecutable: Boolean = false,
    val isDirectory: Boolean = false
) {
    companion object {
        private const val REGULAR_MODE = "100644"
        private const val EXECUTABLE_MODE = "100755"
        private const val DIRECTORY_MODE = "040000"
    }

    val mode: String by lazy {
        if (isDirectory) return@lazy DIRECTORY_MODE

        if (isExecutable) EXECUTABLE_MODE else REGULAR_MODE
    }
}