package com.example.alphens.data.repository.note

import android.net.Uri
import com.example.alphens.data.model.Note
import com.example.alphens.data.model.User
import com.example.alphens.util.FireStoreCollection
import com.example.alphens.util.FireStoreDocumentField
import com.example.alphens.util.FirebaseStorageConstants.NOTE_IMAGES
import com.example.alphens.util.Resource
import com.google.firebase.FirebaseException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.Date

class NoteRepositoryImpl(
    val db: FirebaseFirestore,
    val storageReference: StorageReference
) : NoteRepository {
    override fun getNotes(user: User?, result: (Resource<List<Note>>) -> Unit) {
        db.collection(FireStoreCollection.NOTE)
            .whereEqualTo(FireStoreDocumentField.USER_ID, user?.id)
            .orderBy(FireStoreDocumentField.DATE, Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener {
                val notes = arrayListOf<Note>()
                for (document in it) {
                    val note = document.toObject(Note::class.java)
                    notes.add(note)
                }
                result.invoke(
                    Resource.Success(notes)
                )
            }
            .addOnFailureListener {
                result.invoke(
                    Resource.Error(
                        it.localizedMessage
                    )
                )
            }
    }

    override fun createNote(note: Note, result: (Resource<Pair<Note, String>>) -> Unit) {
        val document = db.collection(FireStoreCollection.NOTE).document()
        note.id = document.id
        document
            .set(note)
            .addOnSuccessListener {
                result.invoke(
                    Resource.Success(Pair(note, "success"))
                )
            }
            .addOnFailureListener {
                result.invoke(
                    Resource.Error(
                        it.localizedMessage
                    )
                )
            }
    }

    override fun updateNote(note: Note, result: (Resource<String>) -> Unit) {
        val document = db.collection(FireStoreCollection.NOTE).document(note.id)
        document
            .set(note)
            .addOnSuccessListener {
                result.invoke(
                    Resource.Success("updated")
                )
            }
            .addOnFailureListener {
                result.invoke(
                    Resource.Error(
                        it.localizedMessage
                    )
                )
            }
    }

    override fun deleteNote(note: Note, result: (Resource<String>) -> Unit) {
        val document = db.collection(FireStoreCollection.NOTE).document(note.id)
        document
            .delete()
            .addOnSuccessListener {
                result.invoke(
                    Resource.Success("deleted")
                )
            }
            .addOnFailureListener {
                result.invoke(
                    Resource.Error(
                        it.localizedMessage
                    )
                )
            }
    }
}