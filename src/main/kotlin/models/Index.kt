package com.sunniercherries.models

import com.sunniercherries.FILE_SYSTEM
import com.sunniercherries.INDEX_FILE_PATH
import okio.Buffer
import okio.ByteString
import java.nio.file.Path
import java.nio.file.attribute.BasicFileAttributes
import java.nio.file.attribute.FileTime
import kotlin.io.path.isExecutable
import kotlin.io.path.pathString
import kotlin.io.path.getAttribute

class Index {
    companion object {
        const val REGULAR_MODE = 33188
        const val EXECUTABLE_MODE = 33261
        const val MAX_PATH_SIZE = 0xfff

        const val SIGNATURE = "DIRC"
        const val SUPPORTED_VERSION = 2
    }

    private val entries = mutableListOf<Entry>()

    data class Entry(
        val path: Path,
        val oid: ByteString,
        val attributes: BasicFileAttributes
    ) {
        private val pathString: String
            get() = path.pathString

        private val mode: Int
            get() = if (path.isExecutable()) EXECUTABLE_MODE else REGULAR_MODE

        private val flags: Int
            get() = minOf(pathString.toByteArray().size, MAX_PATH_SIZE)

        private val size: Long
            get() = attributes.size()

        private val cTime = (path.getAttribute("unix:ctime") as FileTime).toInstant()
        private val cTimeSec: Int = cTime.epochSecond.toInt()
        private val cTimeNano: Int = cTime.nano

        private val mTimeSec: Int
            get() = attributes.lastModifiedTime().toInstant().epochSecond.toInt()
        private val mTimeNano: Int = attributes.lastModifiedTime().toInstant().nano

        private val dev = path.getAttribute("unix:dev") as Long
        private val ino = path.getAttribute("unix:ino") as Long
        private val uid = path.getAttribute("unix:uid") as Int
        private val gid = path.getAttribute("unix:gid") as Int

        fun encode(): ByteArray {
            return Buffer().apply {
                writeInt(cTimeSec) // 32-bit seconds, big-endian
                writeInt(cTimeNano)  // 32-bit nanoseconds, big-endian

                writeInt(mTimeSec)  // 32-bit seconds, big-endian
                writeInt(mTimeNano)  // 32-bit nanoseconds, big-endian

                writeInt(dev.toInt()) //32-bit
                writeInt(ino.toInt()) //32-bit
                writeInt(mode)//32-bit
                writeInt(uid)//32-bit
                writeInt(gid)//32-bit
                writeInt(this@Entry.size.toInt())  // Git uses 32-bit for file size in the index
                write(oid)
                writeShort(flags) // 16-bit
                writeUtf8(pathString)//pathname

                // Add padding to ensure total bytes are a multiple of 8
                val currentSize = size
                //TODO: TRY TO UNDERSTAND THIS
                val paddingNeeded = (8 - (currentSize % 8)) % 8
                repeat(paddingNeeded.toInt()) {
                    writeByte(0)
                }
            }.readByteArray()
        }
    }

    fun add(
        path: Path,
        oid: ByteString,
        attributes: BasicFileAttributes,
    ) {
        val entry = Entry(path, oid, attributes)
        entries.add(entry)
    }

    fun writeToIndexFile() {
        val entriesBuffer = Buffer().run {
            //write header
            writeUtf8(SIGNATURE)
            writeInt(SUPPORTED_VERSION)
            writeInt(entries.size)

            //write body
            entries.forEach { entry ->
                write(entry.encode())
            }

            // GOTCHA READING FROM THE BUFFER CLEARS IT. THAT MEANS WHEN WE USE APPLY SCOPE FUNCTION AND
            // READ BYTE_ARRAY METHOD THE BUFFER IS CLEARED BEFORE SHA-1 HASH IS CALCULATED PRODUCING INCORRECT HASHES
            // https://github.com/square/okio/issues/95#issuecomment-67988511
            readByteString()
        }


        FILE_SYSTEM.write(INDEX_FILE_PATH) {
            write(entriesBuffer)
            write(entriesBuffer.sha1())
        }
    }
}
