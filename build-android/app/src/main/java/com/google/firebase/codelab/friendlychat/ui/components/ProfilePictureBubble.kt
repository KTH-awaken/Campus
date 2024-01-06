package com.google.firebase.codelab.friendlychat.ui.components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter

@Composable
fun ProfilePictureBubble(photoUrl: String, imageSize: Dp) {
    //Djinkigkahn Gineus metod här rikigt bra tänkade
    var isRealPhotoUrl = (photoUrl.length>60)
    var color = Color.Black
    if (!isRealPhotoUrl){
        val hashCode = photoUrl.hashCode()
        val red = (hashCode and 0xFF0000 shr 16) / 255.0f
        val green = (hashCode and 0x00FF00 shr 8) / 255.0f
        val blue = (hashCode and 0x0000FF) / 255.0f
        color = Color(red, green, blue)
    }
    Card(
        shape = CircleShape,
        modifier = Modifier
            .size(imageSize)
            .padding(4.dp)
            .border(1.dp, MaterialTheme.colorScheme.onTertiary, CircleShape)
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
                if(photoUrl=="null")firstCharOfPhotoUrl= 'B'//todo remvoe when databse is fixed
                Log.d("MarcusTAG", "ProfilePictureBubble: $photoUrl")
                Text(
                    text = firstCharOfPhotoUrl.toString(),
                    color = Color.White,
                    fontSize = 14.sp,
                    )
            }
        }
    }
}