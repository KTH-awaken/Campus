package com.google.firebase.codelab.friendlychat.ui.screens

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.ColumnScopeInstance.weight
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*

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
import com.google.firebase.codelab.friendlychat.ui.components.CustomBasicTextField
import com.google.firebase.codelab.friendlychat.ui.components.StatusBar
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
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
            .padding(bottom = 10.dp)
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChatCard(vm: ChatVM){
    val messages by vm.messages.collectAsState()
    val scrollState = rememberLazyListState()
    ElevatedCard(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onBackground,),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp,),

        modifier = Modifier
            .fillMaxWidth()
//            .weight(1f)
            .padding(bottom = 0.dp)
            .size(height = 684.dp, width = 100.dp),
    ){
        Column {

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding()
                ,
                state = scrollState
            ) {
                items(messages.size) { index ->
                    val message = messages[index]
                    MessageBubble(message,vm)
                }
            }
            CustomBasicTextField(vm = vm)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)//TODO CHECK IF CAN BE REMOVED IF CAUSES PROBLEMS AND CAUSE ITS UGLY
@Composable
fun MessageBubble(message: Message,vm: ChatVM) {
    var textColor = Color.Black
    val darkMode by vm.darkMode.collectAsState()
    if (!darkMode&&vm.isMyMessage(message)){
            textColor = Color.White
    }
    val timestamp = message.timeStamp?.toLongOrNull() ?: System.currentTimeMillis()
    val messageTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault())
    val formattedTime = if (messageTime.toLocalDate().isEqual(LocalDate.now())) {
        messageTime.format(DateTimeFormatter.ofPattern("HH:mm"))
    } else {
        messageTime.format(DateTimeFormatter.ofPattern("d MMM HH:mm"))
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 3.dp),
        horizontalArrangement = if (vm.isMyMessage(message)) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (!vm.isMyMessage(message)){
            ProfilePictureBubble(message.photoUrl ?: "", 37.dp)
            Spacer(modifier = Modifier.size(2.dp))
        }else{
            Text(
                modifier = Modifier
                    .padding(end = 4.dp, top = 15.dp),
                text=formattedTime,
                color = MaterialTheme.colorScheme.secondary,
                fontSize = 10.sp,
            )
            //TODO ADD TEXT OF CURRENT ROM IN BOLD

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
        }else{
            Text(
                modifier = Modifier
                    .padding(start = 4.dp, top = 15.dp),
                text=formattedTime,
                color = MaterialTheme.colorScheme.secondary,
                fontSize = 10.sp,
            )
            //TODO ADD TEXT OF CURRENT ROM IN BOLD
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
