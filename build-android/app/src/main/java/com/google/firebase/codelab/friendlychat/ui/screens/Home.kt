package com.example.campus.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.example.campus.ui.components.ColorChangingCampusLogo
import com.example.campus.ui.viewmodels.ChatVM
import com.google.firebase.codelab.friendlychat.R
import com.google.firebase.codelab.friendlychat.ui.components.ProfilePictureBubble
import com.google.firebase.codelab.friendlychat.ui.viewmodels.LocationVM
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Home(vm: ChatVM, navController: NavController, locationVM: LocationVM) {
    Column(
        modifier = Modifier
            .padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 10.dp)
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TopButtonBar(vm = vm,navController = navController)
        Chats(vm = vm,navController = navController,locationVM = locationVM)


    }
}
@Composable
fun TopButtonBar(vm: ChatVM, navController: NavController) {
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
           ColorChangingCampusLogo(vm = vm,navController = navController)
        }
        Button(
            onClick = { /*TODO add campus*/ },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onBackground),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.size(130.dp, 40.dp),
            contentPadding = PaddingValues(0.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.baseline_add_24),
                    contentDescription = "Add",
                    modifier = Modifier.size(24.dp),
                    tint = MaterialTheme.colorScheme.primary,
                )
                Text(
                    text = " Add Campus ",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 13.sp,
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Chats(vm: ChatVM,navController: NavController,locationVM: LocationVM){
    ElevatedCard(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onBackground,),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp,),
        modifier = Modifier
            .fillMaxWidth()
            .size(height = 900.dp, width = 100.dp),
    ){

        ChatPreview(vm =vm ,navController = navController)
        Button(onClick = { locationVM.updateLocation() }) {
            Text("Update Location")
        }
        Button(onClick = { locationVM.saveRoom() }) {
            Text("Save room")
        }
        Button(onClick = { locationVM.getMyCurrentRoomName() }) {
            Text("Save room")
        }
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.BottomEnd
        ) {
            IconButton(
                onClick = { navController.navigate("settings") },
                modifier = Modifier.padding(12.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.outline_settings_24),
                    contentDescription = "Settings",
                    tint = MaterialTheme.colorScheme.secondary
                )
            }

        }
    }
}



@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChatPreview(vm: ChatVM, navController: NavController) {
    val messages by vm.messages.collectAsState()

    val lastMessage = messages.lastOrNull()


    val timestamp = lastMessage?.timeStamp?.toLongOrNull() ?: System.currentTimeMillis()
    val messageTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault())
    val formattedTime = if (messageTime.toLocalDate().isEqual(LocalDate.now())) {
        messageTime.format(DateTimeFormatter.ofPattern("HH:mm"))
    } else {
        messageTime.format(DateTimeFormatter.ofPattern("d MMM HH:mm"))
    }
    Row(
        modifier = Modifier
            .clickable { navController.navigate("chat") }
            .padding(16.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ProfilePictureStack(vm.getAllMembersProfilePhotosFromChat())
        Spacer(modifier = Modifier.width(8.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "FLEMINGSBERG",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 18.sp,
            )
            Spacer(modifier = Modifier.height(2.dp))

            if (lastMessage != null) {
                val firstName = lastMessage.name?.split(" ")?.firstOrNull()
                if(firstName!=null){
                    Text(
                        text = ("$firstName: " + lastMessage.text) ?: "",
                        color = MaterialTheme.colorScheme.secondary,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 13.sp,
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = formattedTime,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.padding(bottom = 30.dp),
            fontSize = 13.sp,
        )
    }
}

@Composable
fun ProfilePictureStack(profileUrls: List<String>) {
    val uniqueUrls = profileUrls.distinct().takeLast(2)
    val firstUniqueUrl = uniqueUrls.firstOrNull()
    val secondUniqueUrl = uniqueUrls.getOrNull(1)

    BoxWithConstraints() {
        Box() {
            if (firstUniqueUrl != null) {
                ProfilePictureBubble(photoUrl = firstUniqueUrl, imageSize =37.dp )
            }
        }
        Column() {
            Spacer(modifier = Modifier
                .height(15.dp)
                .width(10.dp))
            Row() {
                Spacer(modifier = Modifier.width(15.dp))
                Box(
                    modifier = Modifier
                        .zIndex(2f)
                ) {
                    if (secondUniqueUrl != null) {
                        ProfilePictureBubble(photoUrl = secondUniqueUrl, imageSize =37.dp )
                    }

                }
            }
        }
    }

}



