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
import okhttp3.Call
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

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

    /*fun sendMessage(//old working
        text: String,
        userName: String?,
        photoUrl: String?,
        timeStamp: String?,
        currentRoom: String?
    ){
        var photoUrlToSave = photoUrl

        if (photoUrl==null||photoUrl==""||photoUrl=="null"){
            photoUrlToSave=userName
        }
        val message = Message(text, userName, photoUrlToSave, null, timeStamp, currentRoom)

        messagesRef.push().setValue(message)
        //todo send notificaton from here saying that ther is an unred messeg from campus flemingsber uing this server key
        AAAA3OIgC3g:APA91bFt16Ut5eIJBTLySg2xCuctBUxU46WbhzTTQg6R1NPNpbmOf2JWQEZQ6jJdmP6Qn9m6jcStJOXURXpEIby3J4lfTy5zaHa1-4ZsspNtsjlz1Ic8tGV-SaQ5DLQDGCsp72z9f3wO server key
    }
*/

    fun sendMessage(
        text: String,
        userName: String?,
        photoUrl: String?,
        timeStamp: String?,
        currentRoom: String?
    ) {
        var photoUrlToSave = photoUrl

        if (photoUrl==null||photoUrl==""||photoUrl=="null"){
            photoUrlToSave=userName
        }
        val message = Message(text, userName, photoUrlToSave, null, timeStamp, currentRoom)

        messagesRef.push().setValue(message).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                sendNotificationToEveryone("New message from $userName", text)
            } else {
                Log.e("ChatVM", "Failed to send message")
            }
        }
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

    private fun updateUserProfilePhoto() {
        val user = auth.currentUser
        if (user != null) {
            val userRef = db.getReference("users/${user.uid}")
            if (auth.currentUser?.displayName == "null" || auth.currentUser == null) {
                userRef.child("photoUrl").setValue(auth.currentUser?.displayName)
            }
        }
    }

    private fun sendNotificationToEveryone(messageText: String, roomName: String) {
        usersRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val title = "New message in $roomName"
                snapshot.children.forEach { child ->
                    val user = child.getValue(User::class.java)
                    user?.fcmToken?.let { token ->
                        sendNotification(token, title, messageText)
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("MarcusTAG", "Failed to send message", databaseError.toException())
            }
        })
    }

    fun sendNotification(token: String, title: String, message: String) {
        Log.d("MarcusTAG", "sendNotification: $token")
        val client = OkHttpClient()

        val requestBody = FormBody.Builder()
            .add("to", token)
            .add("priority", "high")
            .add("notification", JSONObject().apply {
                put("title", title)
                put("body", message)
            }.toString())
            .build()

        val request = Request.Builder()
            .url("https://fcm.googleapis.com/fcm/send")
            .post(requestBody)
            .addHeader("Authorization", "key=AAAA3OIgC3g:APA91bFt16Ut5eIJBTLySg2xCuctBUxU46WbhzTTQg6R1NPNpbmOf2JWQEZQ6jJdmP6Qn9m6jcStJOXURXpEIby3J4lfTy5zaHa1-4ZsspNtsjlz1Ic8tGV-SaQ5DLQDGCsp72z9f3wO")
            .addHeader("Content-Type", "application/json")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                Log.d("MarcusTAG", "FCM response: ${response.body?.string()}")
                if (response.isSuccessful) {
//                    Log.d("MarcusFCM", "Notification sent successfully. Response: ${response.body?.string()}")
                } else {
//                    Log.e("MarcusFCM", "Failed to send notification. Response: ${response.body?.string()}")
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                Log.e("NetworkCall", "sendNotification: Failed to send notification", e)
            }
        })
    }
}

