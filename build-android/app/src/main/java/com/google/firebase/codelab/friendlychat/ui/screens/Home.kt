package com.example.campus.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.campus.ui.components.ColorChangingCampusLogo
import com.example.campus.ui.viewmodels.ChatVM
import com.google.firebase.codelab.friendlychat.R

@Composable
fun Home(vm: ChatVM,navController: NavController) {
    Column(
        modifier = Modifier
            .padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 20.dp)
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TopButtonBar(vm = vm)
        Chats(vm = vm,navController = navController)


    }
}
@Composable
fun TopButtonBar(vm: ChatVM) {
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

@Composable
fun Chats(vm: ChatVM,navController: NavController){
    ElevatedCard(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onBackground,),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp,),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp)
            .size(height = 684.dp, width = 100.dp),
    ){
        //other stuff lazy column with chats
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
