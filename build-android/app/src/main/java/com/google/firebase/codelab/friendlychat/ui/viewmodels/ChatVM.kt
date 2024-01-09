package com.example.campus.ui.viewmodels

import android.util.Log
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.codelab.friendlychat.model.Message
import com.google.firebase.codelab.friendlychat.model.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class ChatVM(
     val db: FirebaseDatabase,
     val messagesRef: DatabaseReference,
     val auth: FirebaseAuth,
    val usersRef: DatabaseReference
): ViewModel(){
    //class data

    var isSystemInDarkTheme = MutableStateFlow(false)
    private var _darkMode = MutableStateFlow<Boolean>(isSystemInDarkTheme.value)
    val darkMode: StateFlow<Boolean> get() = _darkMode
    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages
    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users: StateFlow<List<User>> get() = _users

    private val _textInput = MutableStateFlow("")
    val textInput: StateFlow<String> get() = _textInput

    //init
    init {
        listenForMessages()
        listenForUsers()
    }

    //class funktioner
    private fun listenForMessages() {
        messagesRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val messagesList = snapshot.children.mapNotNull { it.getValue(Message::class.java) }
                _messages.value = messagesList
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("ChatVM", "Error listening for messages", error.toException())
            }
        })
    }

    private fun listenForUsers() {
        usersRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val usersList = snapshot.children.mapNotNull { it.getValue(User::class.java) }
                _users.value = usersList
                Log.d("MarcusTAG users",usersList.toString())
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("ChatVM", "Error listening for users", error.toException())
            }
        })
    }

    fun sendMessage(
        text: String,
        userName: String?,
        photoUrl: String?,
        timeStamp: String?,
        currentRoom: String?
    ){
        var photoUrlToSave = photoUrl
        if (photoUrl==null||photoUrl==""){
            photoUrlToSave=userName
        }
        val message = Message(text, userName, photoUrlToSave, null, timeStamp, currentRoom)
        if (currentRoom != null) {
            updateUser(currentRoom)
        }
        messagesRef.push().setValue(message)
    }

    fun setDarkMode(value: Boolean){
        _darkMode.value = value
        Log.d("MarcusTAG2", "darkMode: ${_darkMode.value}")
    }

    fun setSystemInDarkTheme(value: Boolean){
        isSystemInDarkTheme.value = value
        setDarkMode(isSystemInDarkTheme.value)
        Log.d("MarcusTAG system dark them", "isSystemInDarkTheme: ${isSystemInDarkTheme.value}")
        Log.d("MarcusTAG2 system", "darkMode: ${_darkMode.value}")
    }

    fun isMyMessage(message: Message): Boolean {
        return message.name == auth.currentUser?.displayName
    }

    fun setTextInput(value: String){
        _textInput.value = value
    }

    fun getAllMembersProfilePhotosFromChat(): List<String> {
        val allMembersProfilePhotos = mutableListOf<String>()
        for (message in _messages.value) {
            if (message.photoUrl != null) {
                allMembersProfilePhotos.add(message.photoUrl)
            }
        }
        return allMembersProfilePhotos
    }

    fun getFirstName(Message: Message): String {
        return Message.name?.split(" ")?.get(0) ?: "Unknown"
    }

    fun signOut(){
        auth.signOut()
    }

    private fun Message.isNewerThan(other: Message): Boolean {
        val thisTimeStamp = this.timeStamp?.toLongOrNull() ?: 0L
        val otherTimeStamp = other.timeStamp?.toLongOrNull() ?: 0L
        return thisTimeStamp > otherTimeStamp
    }

    fun getMessagesForRoom(roomName: String): List<Message> {
        return _messages.value.filter { it.room == roomName }
    }

    fun updateUser(roomName: String){
        val user = auth.currentUser
        if (user != null) {
            val userRef = db.getReference("users/${user.uid}")
            userRef.child("room").setValue(roomName)
        }
    }

    fun updateUserOnInterval(delay: Long,roomName: String){
        viewModelScope.launch {
            while (isActive) {
                updateUser(roomName)
                delay(delay)
            }
        }
    }

    fun getAllLatestMessagesFomEachUser(): List<Message> {//TODO REOMVE
        val latestMessagesMap = mutableMapOf<String, Message>()

        for (message in _messages.value) {
            val userName = message.name
            if (userName != null) {
                val existingMessage = latestMessagesMap[userName]
                if (existingMessage == null || message.isNewerThan(existingMessage)) {
                    latestMessagesMap[userName] = message
                }
            }
        }
        return latestMessagesMap.values.toList()
    }

    fun getAllMembersUniqueUrlsProfilePhotosFromChat(messages :List<Message>): List<String> {//TODO REOMVE
        val membersProfilePhotos = mutableListOf<String>()

        for (message in messages) {
            if (message.photoUrl != null) {
                membersProfilePhotos.add(message.photoUrl)
            }
        }
        return membersProfilePhotos.distinct()
    }
}

