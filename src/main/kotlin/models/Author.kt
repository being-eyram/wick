package com.sunniercherries.models

import kotlinx.datetime.*

data class Author(
    val name: String,
    val email: String,
) {
    private val time: String
        get() {
            val instant = Clock.System.now()
            val epochSeconds = instant.epochSeconds

            val timeZoneOffset = TimeZone.currentSystemDefault()
                .offsetAt(instant)
                .format(UtcOffset.Formats.FOUR_DIGITS)

            return "$epochSeconds $timeZoneOffset"
        }

    override fun toString(): String {
        return "$name, $email, $time"
    }
}
