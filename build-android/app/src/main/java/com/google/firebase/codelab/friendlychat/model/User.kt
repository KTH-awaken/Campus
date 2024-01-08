package com.google.firebase.codelab.friendlychat.model

data class User(
    val uid: String? = null,
    val displayName: String? = null,
    val photoUrl: String? = null,
    val email: String? = null,
    val listOfCampusIds: List<String>? = null,
    val room: String? = null,
    val lastOnline: String? = null,
    val isDarkTheme: Boolean? = false,
)
