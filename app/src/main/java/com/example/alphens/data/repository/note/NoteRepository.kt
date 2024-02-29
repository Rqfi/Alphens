package com.example.alphens.data.repository.note

import android.net.Uri
import com.example.alphens.data.model.Note
import com.example.alphens.data.model.User
import com.example.alphens.util.Resource

interface NoteRepository {
    fun getNotes(
        user: User?,
        result: (Resource<List<Note>>) -> Unit
    )
    fun createNote(
        note: Note,
        result: (Resource<Pair<Note, String>>) -> Unit
    )
    fun updateNote(
        note: Note,
        result: (Resource<String>) -> Unit
    )
    fun deleteNote(
        note: Note,
        result: (Resource<String>) -> Unit
    )
}