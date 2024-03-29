package com.google.firebase.codelab.friendlychat.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.campus.ui.viewmodels.ChatVM
import com.google.firebase.codelab.friendlychat.model.User

@Composable
fun Room(vm:ChatVM, listOfUserInRoom: List<User>, roomName: String) {

    val usersInRoom = listOfUserInRoom.filter { user -> user.room == roomName }
    Card(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .border(2.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(6.dp))
            .background(MaterialTheme.colorScheme.onBackground)

    ) {
        Column (
                modifier = Modifier.background( MaterialTheme.colorScheme.onBackground),
        ){
            Text(
                text = roomName,
//                text = "Makerspace",
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .background(color = MaterialTheme.colorScheme.onBackground),
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.secondary,
                textAlign = TextAlign.Start,
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.background( MaterialTheme.colorScheme.onBackground),
            ) {

                usersInRoom.forEach { user ->
                    if (user.photoUrl!=null&&user.photoUrl!=""&&user.photoUrl!="null"){
                        user.photoUrl?.let { ProfilePictureBubble(photoUrl = it, imageSize = 37.dp,user) }
                    }
                    if (user.photoUrl==null||user.photoUrl==""||user.photoUrl=="null"){
                        user.displayName?.let { ProfilePictureBubble(photoUrl = it, imageSize = 37.dp,user) }
                    }

                }
            }
        }
    }
}
