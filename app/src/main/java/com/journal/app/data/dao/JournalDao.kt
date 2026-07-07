package com.journal.app.data.dao

import androidx.room.*
import com.journal.app.data.entity.JournalEntry
import kotlinx.coroutines.flow.Flow

@Dao
interface JournalDao {

    @Query("SELECT * FROM journal_entries ORDER BY date DESC")
    fun getAllEntries(): Flow<List<JournalEntry>>

    @Query("SELECT date FROM journal_entries WHERE date LIKE :yearMonth || '%'")
    fun getDatesForMonth(yearMonth: String): Flow<List<String>>

    @Query("SELECT * FROM journal_entries WHERE date = :date")
    suspend fun getEntryByDate(date: String): JournalEntry?

    @Query("SELECT * FROM journal_entries WHERE date = :date")
    fun observeEntryByDate(date: String): Flow<JournalEntry?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(entry: JournalEntry)

    @Delete
    suspend fun delete(entry: JournalEntry)
}
