package com.example.alphens.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.alphens.data.model.Note
import com.example.alphens.data.model.User
import com.example.alphens.data.repository.note.NoteRepository
import com.example.alphens.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: NoteRepository
): ViewModel() {

    private val _notes = MutableLiveData<Resource<List<Note>>>()
    val notes: LiveData<Resource<List<Note>>>
        get() = _notes

    private val _create = MutableLiveData<Resource<Pair<Note, String>>>()
    val create: LiveData<Resource<Pair<Note, String>>>
        get() = _create

    private val _update = MutableLiveData<Resource<String>>()
    val update: LiveData<Resource<String>>
        get() = _update

    private val _delete = MutableLiveData<Resource<String>>()
    val delete: LiveData<Resource<String>>
        get() = _delete

    fun getNotes(user: User?) {
        _notes.value = Resource.Loading
        repository.getNotes(user) {
            _notes.value = it
        }
    }

    fun createNote(note: Note) {
        _create.value = Resource.Loading
        repository.createNote(note) {
            _create.value = it
        }
    }

    fun updateNote(note: Note) {
        _update.value = Resource.Loading
        repository.updateNote(note) {
            _update.value = it
        }
    }

    fun deleteNote(note: Note) {
        _delete.value = Resource.Loading
        repository.deleteNote(note) {
            _delete.value = it
        }
    }
}