package com.google.firebase.codelab.friendlychat.ui.components

import android.util.Log
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.campus.ui.viewmodels.ChatVM

@Composable
fun StatusBar(vm: ChatVM) {
    ElevatedCard(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onBackground,),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp,),

        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp)
            .size(height = 62.dp, width = 100.dp),
    ){
        val allLatestMessages = vm.getAllLatestMessagesFomEachUser()
        val uniqueRoomMessages = allLatestMessages.distinctBy { it.room }
        Log.d("MarcusTAGlATESTMESSAGES", allLatestMessages.toString())
        LazyRow (
            modifier = Modifier.padding(5.dp)
        ){
            items(uniqueRoomMessages) { message ->
                Room(
                    vm = vm,
                    listOfMessegesFromRoom = vm.getMessagesForRoom(message.room ?: ""),
                    roomName = message.room ?: "Unknown Room from StatusBar.kt"
                )
                Spacer(modifier =Modifier.size(5.dp))
            }
        }
    }

}