package com.google.firebase.codelab.friendlychat.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.example.campus.ui.viewmodels.ChatVM

@Composable
fun Room(vm:ChatVM) {
    val profilePictures = vm.getAllMembersUniqueUrlsProfilePhotosFromChat()
    Card(
        modifier = Modifier
            .padding(6.dp)
            .clip(RoundedCornerShape(6.dp))
            .border(1.dp, MaterialTheme.colorScheme.secondary, RoundedCornerShape(6.dp))
            .padding(6.dp)
            .background(MaterialTheme.colorScheme.onBackground),
    ) {

        Row (
            modifier = Modifier.background( MaterialTheme.colorScheme.onBackground),
        ){
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.background( MaterialTheme.colorScheme.onBackground),
        ) {
            profilePictures.forEach { imageUrl ->
                Card(
                    shape = CircleShape,
                    modifier = Modifier
                        .size(37.dp)
                        .padding(start = 4.dp, end = 4.dp,top= 4.dp,bottom = 4.dp)
                ) {
                    Image(
                        painter = rememberImagePainter(imageUrl),
                        contentDescription = "Profile picture",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                            .border(1.dp, MaterialTheme.colorScheme.outline, CircleShape)
                    )
                }
            }
            Text(
                text = "Rudan",
                modifier = Modifier
                    .padding(start = 8.dp)
                    .background(color = MaterialTheme.colorScheme.onBackground),
                fontSize = 3.sp,
                color = MaterialTheme.colorScheme.secondary,
            )


        }
    }
}