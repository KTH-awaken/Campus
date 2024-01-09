package com.google.firebase.codelab.friendlychat.ui.components

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
        val users by vm.users.collectAsState()
        val allUsersDistinctByRoom = users.distinctBy { it.room }
        LazyRow (
            modifier = Modifier.padding(5.dp)
        ){
            items(allUsersDistinctByRoom) { user ->
                if(user.room != null){
                    Room(
                        vm = vm,
                        listOfUserInRoom = users.filter { it.room == user.room },
                        roomName = user.room
                    )
                    Spacer(modifier =Modifier.size(5.dp))
                }
            }
        }
    }

}