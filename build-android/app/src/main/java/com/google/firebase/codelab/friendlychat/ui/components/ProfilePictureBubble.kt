package com.google.firebase.codelab.friendlychat.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.google.firebase.codelab.friendlychat.model.User
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@Composable
    //Djinkigkahn Gineus metod här rikigt bra tänkade
fun ProfilePictureBubble(photoUrl: String, imageSize: Dp, user: User?) {
    var showDialog by remember { mutableStateOf(false) }
    var isRealPhotoUrl = (photoUrl.length>60)
    var color = Color.Black
    if (!isRealPhotoUrl){
        val hashCode = photoUrl.hashCode()
        val red = (hashCode and 0xFF0000 shr 16) / 255.0f
        val green = (hashCode and 0x00FF00 shr 8) / 255.0f
        val blue = (hashCode and 0x0000FF) / 255.0f
        color = Color(red, green, blue)
    }

    var borderColor = MaterialTheme.colorScheme.onTertiary

    if (user != null && user.lastOnline != null) {
        val lastOnlineTime = LocalTime.parse(user.lastOnline, DateTimeFormatter.ofPattern("HH:mm"))
        val now = LocalTime.now()
        if (ChronoUnit.MINUTES.between(lastOnlineTime, now) <= 2) {
            borderColor = MaterialTheme.colorScheme.tertiary
        }
    }

    Card(
        shape = CircleShape,
        modifier = Modifier
            .size(imageSize)
            .padding(4.dp)
            .border(1.dp, borderColor, CircleShape)
            .clickable { if (user != null) showDialog = true }
        ,
    ) {
        if(isRealPhotoUrl){
            Image(
                painter = rememberImagePainter(data = photoUrl),
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }else{
            Box(modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape)
                .background(color = color),
               contentAlignment = Alignment.Center
            ) {
                var firstCharOfPhotoUrl = photoUrl[0]
                Text(
                    text = firstCharOfPhotoUrl.toString(),
                    color = Color.White,
                    fontSize = 14.sp,
                    )
            }
        }
        if (showDialog && user != null) {
            UserDetailsDialog(user = user, onDismissRequest = { showDialog = false })
        }
    }
}