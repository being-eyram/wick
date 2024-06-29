package com.sunniercherries.models

import com.sunniercherries.FILE_SYSTEM
import java.io.ByteArrayOutputStream
import java.util.zip.Deflater


object Database {
    fun store(snappable: Snappable) = writeObject(
        id = snappable.hash,
        content = snappable.payload
    )

    private fun writeObject(
        id: String, content: ByteArray,
    ): Boolean {
        val objectDir = WorkSpace.OBJECTS_PATH.resolve(id.substring(0, 2))
        val objectFilePath = objectDir.resolve(id.substring(2))

        if (FILE_SYSTEM.exists(objectFilePath)) return true;

        val tempFile = objectDir.resolve(generateTempName())

        val result = runCatching {
            val compressedContent = compress(content)

            FILE_SYSTEM.apply {
                createDirectories(objectDir)

                write(tempFile) {
                    write(compressedContent)
                }
                atomicMove(tempFile, objectFilePath)
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