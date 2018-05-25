package org.bk.univ

import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date


object DateTimeUtils {
    fun toDate(dateToConvert: LocalDateTime): Date {
        return Date.from(dateToConvert.atZone(ZoneId.systemDefault())
                .toInstant())
    }

    fun toDatetime(dateToConvert: Date): LocalDateTime {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime()
    }
}