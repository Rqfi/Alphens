package com.example.alphens.util

object FireStoreCollection {
    val NOTE = "note"
    val USER = "user"
}

object FireStoreDocumentField {
    val DATE = "date"
    val USER_ID = "user_id"
}

object SharedPrefConstants {
    val LOCAL_SHARED_PREF = "local_shared_pref"
    val USER_SESSION = "user_session"
}

object FirebaseStorageConstants {
    val ROOT_DIRECTORY = "app"
    val NOTE_IMAGES = "note"
}

enum class HomeTabs(val index: Int, val key: String) {
    NOTES(0, "notes"),
}