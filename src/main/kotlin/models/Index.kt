package com.sunniercherries.models

import com.sunniercherries.FILE_SYSTEM
import com.sunniercherries.INDEX_FILE_PATH
import okio.Buffer
import okio.ByteString
import java.nio.file.Path
import java.nio.file.attribute.BasicFileAttributes
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

        private val cTimeMillis: Int
            get() = attributes.creationTime().toMillis().toInt()

        private val mTimeMillis: Int
            get() = attributes.lastModifiedTime().toMillis().toInt()

        private val size: Long
            get() = attributes.size()

        private val cTimeNano: Int = 0
        private val mTimeNano: Int = 0

        private val dev: Int = path.getAttribute("unix:dev") as Int
        private val ino: Long = path.getAttribute("unix:ino") as Long
        private val uid: Int = path.getAttribute("unix:uid") as Int
        private val gid: Int = path.getAttribute("unix:gid") as Int


        fun encode(): ByteArray {
            return Buffer().apply {
                writeInt(cTimeMillis)  // 32-bit seconds, big-endian
                writeInt(cTimeNano)  // 32-bit nanoseconds, big-endian

                writeInt(mTimeMillis)  // 32-bit seconds, big-endian
                writeInt(mTimeNano)  // 32-bit nanoseconds, big-endian

                writeInt(dev)
                writeLong(ino)
                writeInt(mode)
                writeInt(uid)
                writeInt(gid)
                writeInt(size.toInt())  // Git uses 32-bit for file size in the index
                write(oid)
                writeShort(flags)
                writeUtf8(pathString)


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
        val entriesBuffer = Buffer().apply {
            //write header
            writeUtf8(SIGNATURE)
            writeInt(SUPPORTED_VERSION)
            writeInt(entries.size)

            //write body
            entries.forEach { entry ->
                write(entry.encode())
            }
        }

        FILE_SYSTEM.write(INDEX_FILE_PATH, mustCreate = true) {
            write(entriesBuffer.readByteArray())
            write(entriesBuffer.sha1())
        }
    }
}
