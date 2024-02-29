package com.example.firebasewithmvvm.di

import android.content.SharedPreferences
import com.example.alphens.data.repository.auth.AuthRepository
import com.example.alphens.data.repository.auth.AuthRepositoryImpl
import com.example.alphens.data.repository.note.NoteRepository
import com.example.alphens.data.repository.note.NoteRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {

    @Provides
    @Singleton
    fun provideNoteRepository(
        database: FirebaseFirestore,
        storageReference: StorageReference
    ): NoteRepository {
        return NoteRepositoryImpl(database,storageReference)
    }

    @Provides
    @Singleton
    fun provideAutghRepository(
        db: FirebaseFirestore,
        auth: FirebaseAuth,
        appPreferences: SharedPreferences,
        gson: Gson
    ): AuthRepository {
        return AuthRepositoryImpl(auth,db,appPreferences,gson)
    }

}