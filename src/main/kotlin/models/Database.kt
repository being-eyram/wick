package com.sunniercherries.models

import okio.*
import okio.Path.Companion.toOkioPath
import okio.Path.Companion.toPath
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.zip.Deflater
import kotlin.io.path.pathString


object Database {
    fun store(snappable: Snappable) = writeObject(
        id = snappable.hash,
        content = snappable.payload
    )

    private fun writeObject(
        id: String, content: ByteArray,
    ): Boolean {
        val objectDir = WorkSpace.OBJECTS_PATH.resolve(id.substring(0, 2))
        val tempFile = objectDir.resolve(generateTempName())
        val objectFile = objectDir.resolve(id.substring(2))

        val result = runCatching {
            val compressedContent = compress(content)

            FileSystem.SYSTEM.apply {
                createDirectories(objectDir)

                write(tempFile) {
                    write(compressedContent)
                }
                atomicMove(tempFile, objectFile)
            }
        }

        return result.isSuccess
    }


    private fun generateTempName(): String {
        val alphaNumericChars = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        val randomChars = (1..6)
            .map { alphaNumericChars.random() }
            .joinToString("")

        return "tmp_obj_$randomChars"
    }

    private fun compress(input: ByteArray): ByteArray {
        val deflater = Deflater().apply {
            setStrategy(Deflater.BEST_SPEED)
            setInput(input)
            finish()
        }

        val outputStream = ByteArrayOutputStream()
        val buffer = ByteArray(1024)

        while (!deflater.finished()) {
            val compressedSize = deflater.deflate(buffer)
            outputStream.write(buffer, 0, compressedSize)
        }

        return outputStream.toByteArray()
    }

}