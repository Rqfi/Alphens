package com.example.alphens.di

import android.content.Context
import android.content.SharedPreferences
import com.example.alphens.data.repository.note.NoteRepository
import com.example.alphens.data.repository.note.NoteRepositoryImpl
import com.example.alphens.util.SharedPrefConstants
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Provides
    @Singleton
    fun provideNoteRepository(
        @ApplicationContext context: Context
    ): SharedPreferences {
        return context.getSharedPreferences(SharedPrefConstants.LOCAL_SHARED_PREF, Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return Gson()
    }
}