package com.google.firebase.codelab.friendlychat.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.campus.ui.components.ColorChangingCampusLogo
import com.example.campus.ui.viewmodels.ChatVM
import com.google.firebase.codelab.friendlychat.model.Message
import coil.compose.rememberImagePainter
import com.google.firebase.codelab.friendlychat.ui.components.CustomInputField
import com.google.firebase.codelab.friendlychat.ui.components.MessageInputBar
import com.google.firebase.codelab.friendlychat.ui.components.StatusBar

@Composable
fun Chat(vm:ChatVM) {
    Column(
        modifier = Modifier
            .padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 20.dp)
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TopBar(vm = vm)
        StatusBar(vm = vm)
        ChatCard(vm = vm)


    }
}


@Composable
fun TopBar(vm: ChatVM) {
    val customCardColors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.onBackground,
    )
    Row(
        modifier = Modifier
            .padding(bottom = 40.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ){
        Box(modifier = Modifier.size(140.dp,44.dp)){
            ColorChangingCampusLogo(vm = vm)
        }
        Text(
            modifier = Modifier.padding(top = 6.dp),
            fontSize = 20.sp,
            text ="FLEMINGSBERG",//TODO REPLACE WITH CAMPUS NAME
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun ChatCard(vm: ChatVM){
    val messages by vm.messages.collectAsState()
    val scrollState = rememberLazyListState()
    ElevatedCard(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onBackground,),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp,),

        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp)
            .size(height = 684.dp, width = 100.dp),
    ){
        Column {

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 20.dp, bottom = 20.dp)
                ,
                state = scrollState
            ) {
                items(messages.size) { index ->
                    val message = messages[index]
                    MessageBubble(message,vm)
                }
            }
//            MessageInputBar(vm)
            CustomInputField(vm = vm)
        }
    }
}

@Composable
fun MessageBubble(message: Message,vm: ChatVM) {
    var textColor = Color.Black
    val darkMode by vm.darkMode.collectAsState()
    if (!darkMode&&vm.isMyMessage(message)){
            textColor = Color.White
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 5.dp),
        horizontalArrangement = if (vm.isMyMessage(message)) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (!vm.isMyMessage(message)){
            ProfilePictureBubble(message.photoUrl ?: "", 37.dp)
            Spacer(modifier = Modifier.size(2.dp))
        }
        Card(
            colors = if (vm.isMyMessage(message)) CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiary) else CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onTertiary),
            shape = RoundedCornerShape(20.dp),

        ) {
            message.text?.let {
                Text(
                    text = it,
                    color = textColor,
                    modifier = Modifier
                        .padding(start = 10.dp, end = 10.dp, top = 3.dp, bottom = 7.dp)
                        ,
                    textAlign = TextAlign.Center,
                    fontSize = 14.sp,
                )
            }
        }
        if (vm.isMyMessage(message)){
            Spacer(modifier = Modifier.size(2.dp))
            ProfilePictureBubble(message.photoUrl ?: "", 37.dp)
        }

    }
}

@Composable
fun ProfilePictureBubble(photoUrl: String, imageSize: Dp) {
    Log.d("Marcus", "ProfilePictureBubble: $photoUrl")
    Card(
        shape = CircleShape,
        modifier = Modifier
            .size(imageSize)
            .padding(4.dp),
    ) {
        Image(
            painter = rememberImagePainter(data = photoUrl),
            contentDescription = "Profile Picture",
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
    }
}
