package com.example.campus.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.codelab.friendlychat.model.Message
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ChatVM(public val db: FirebaseDatabase, public val messagesRef: DatabaseReference, public val auth: FirebaseAuth): ViewModel(){
    //class data
    private var _darkMode = MutableStateFlow<Boolean>(false)
    val darkMode: StateFlow<Boolean> get() = _darkMode
    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages

    private val _textInput = MutableStateFlow("")
    val textInput: StateFlow<String> get() = _textInput


    //init
    init {
        listenForMessages()
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

    fun sendMessage(text: String, userName: String?, photoUrl: String?) {
        val message = Message(text, userName, photoUrl, null)
        messagesRef.push().setValue(message)
    }

    fun setDarkMode(value: Boolean){
        _darkMode.value = value
        Log.d("MarcusTAG2", "darkMode: ${_darkMode.value}")
    }

    fun isMyMessage(message: Message): Boolean {
        return message.name == auth.currentUser?.displayName
    }

    fun setTextInput(value: String){
        _textInput.value = value
    }

}

