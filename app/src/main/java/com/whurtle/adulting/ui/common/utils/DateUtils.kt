package com.whurtle.adulting.ui.common.utils

import kotlinx.datetime.*

class DateUtils {

    companion object {

        fun getNow(): Instant {
            return Clock.System.now()
        }

        fun formatDate(dayOfMoth: Int, month: Int, year: Int): LocalDate {
            return LocalDate(year, month, dayOfMoth)
        }

        fun instantFromEpoch(epoch: Long): LocalDate {
            return Instant.fromEpochMilliseconds(epoch)
                .toLocalDateTime(TimeZone.currentSystemDefault()).date
        }

        fun formatDate(localDate: LocalDate): String {
            return String.format("%d/%d/%d", localDate.dayOfMonth, localDate.month, localDate.year)
        }
    }

}