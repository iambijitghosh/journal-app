package com.journal.app.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "journal_entries")
data class JournalEntry(
    @PrimaryKey
    val date: String, // Format: "yyyy-MM-dd"
    val content: String = "",
    val lastModified: Long = System.currentTimeMillis()
)
