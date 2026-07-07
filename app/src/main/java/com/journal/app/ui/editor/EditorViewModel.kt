package com.journal.app.ui.editor

import android.app.Application
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.journal.app.data.database.JournalDatabase
import com.journal.app.data.entity.JournalEntry
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class EditorViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = JournalDatabase.getDatabase(application).journalDao()

    private val _textValue = MutableStateFlow(TextFieldValue(""))
    val textValue: StateFlow<TextFieldValue> = _textValue.asStateFlow()

    private val _isSaved = MutableStateFlow(true)
    val isSaved: StateFlow<Boolean> = _isSaved.asStateFlow()

    private val _isBold = MutableStateFlow(false)
    val isBold: StateFlow<Boolean> = _isBold.asStateFlow()

    private val _isItalic = MutableStateFlow(false)
    val isItalic: StateFlow<Boolean> = _isItalic.asStateFlow()

    private var currentDate: String = ""
    private var autoSaveJob: Job? = null

    fun loadEntry(date: String) {
        currentDate = date
        viewModelScope.launch {
            val entry = dao.getEntryByDate(date)
            _textValue.value = TextFieldValue(entry?.content ?: "")
        }
    }

    fun onTextChange(newValue: TextFieldValue) {
        _textValue.value = newValue
        _isSaved.value = false
        scheduleAutoSave()
    }

    private fun scheduleAutoSave() {
        autoSaveJob?.cancel()
        autoSaveJob = viewModelScope.launch {
            delay(1500) // Auto-save after 1.5s of inactivity
            save()
        }
    }

    fun save() {
        viewModelScope.launch {
            dao.insertOrUpdate(
                JournalEntry(
                    date = currentDate,
                    content = _textValue.value.text,
                    lastModified = System.currentTimeMillis()
                )
            )
            _isSaved.value = true
        }
    }

    fun toggleBold() {
        _isBold.value = !_isBold.value
    }

    fun toggleItalic() {
        _isItalic.value = !_isItalic.value
    }
}
