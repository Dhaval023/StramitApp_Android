package com.example.stramitapp.models.Database

import androidx.room.TypeConverter
import java.time.OffsetDateTime
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.Date
import java.time.format.DateTimeFormatter

class Converters {
    private val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
    private val localFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    private val localFormatterWithMillis = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")

    @TypeConverter
    fun toOffsetDateTime(value: String?): OffsetDateTime? {
        if (value == null) return null
        return try {
            OffsetDateTime.parse(value, formatter)
        } catch (e: Exception) {
            try {
                LocalDateTime.parse(value, localFormatter).atOffset(ZoneOffset.UTC)
            } catch (e2: Exception) {
                try {
                    LocalDateTime.parse(value, localFormatterWithMillis).atOffset(ZoneOffset.UTC)
                } catch (e3: Exception) {
                    null
                }
            }
        }
    }

    @TypeConverter
    fun fromOffsetDateTime(date: OffsetDateTime?): String? {
        return date?.format(formatter)
    }

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}