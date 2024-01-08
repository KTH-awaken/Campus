package com.example.campus.ui.screens

import android.provider.Settings
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.campus.ui.components.ColorChangingCampusLogo
import com.example.campus.ui.viewmodels.ChatVM
import com.google.firebase.codelab.friendlychat.R

@Composable
fun Settings(vm: ChatVM, navController: NavController) {
        Column(
            modifier = Modifier
                .padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 20.dp)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
            ,
            horizontalAlignment = Alignment.CenterHorizontally,
        ){
            TopBar(vm = vm, navController = navController)
            Spacer(modifier = Modifier.size(20.dp))
            ThemeSelect(vm =  vm)
            UserSettings(vm = vm)
        }
}

@Composable
fun ThemeSelect(vm: ChatVM) {
    val customCardColors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.onBackground,
    )
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
    ){
        Text(
            modifier = Modifier.padding( start = 10.dp, bottom = 5.dp),
            color = MaterialTheme.colorScheme.secondary,
            fontSize = 9.sp,
            text ="Theme"
        )
    }
    ElevatedCard(
        colors = customCardColors,
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp,),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp)
            .size(height = 175.dp, width = 100.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    fontSize = 15.sp,
                    text ="Light",
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 5.dp)
                )
                Button(
                    onClick = { vm.setDarkMode(false) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    shape = RoundedCornerShape(20.dp),
                    border = BorderStroke(4.dp, Color(54,123,247)),
                    contentPadding = PaddingValues(10.dp),
                    modifier = Modifier.size(120.dp, 120.dp)
                ) {

                    Image(
                        painter = painterResource(id = R.drawable.campustextlogo2), // Replace with your image resource
                        contentDescription = "Image Button",
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier
                            .fillMaxSize()
                    )
                }
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    fontSize = 15.sp,
                    text ="Dark",
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 5.dp)
                )
                Button(
                    onClick = { vm.setDarkMode(true) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                    shape = RoundedCornerShape(20.dp),
                    border = BorderStroke(4.dp, Color(220,240,111)),
                    contentPadding = PaddingValues(10.dp),
                    modifier = Modifier.size(120.dp, 120.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.campustextlogo2yellow), // Replace with your image resource
                        contentDescription = "Image Button",
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier
                            .fillMaxSize()
                    )
                }
            }
        }
    }
}

@Composable
fun UserSettings(vm : ChatVM) {
    val customCardColors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.onBackground,
    )
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
    ){
        Text(
            modifier = Modifier.padding( start = 10.dp, bottom = 5.dp),
            color = MaterialTheme.colorScheme.secondary,
            fontSize = 9.sp,
            text ="User"
        )
    }
    ElevatedCard(
        colors = customCardColors,
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp,),
        modifier = Modifier
            .fillMaxWidth()
            .size(height = 200.dp, width = 100.dp)
        ,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .padding(start = 10.dp)
            ,
            verticalAlignment = Alignment.CenterVertically,
        ){
            Button(
                onClick = { vm.signOut() },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onBackground),
                contentPadding = PaddingValues(0.dp),

            ) {
                Row (verticalAlignment = Alignment.CenterVertically){
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_logout_24),
                        contentDescription = "Sign out",
                        tint = MaterialTheme.colorScheme.secondary
                    )
                    Text(text ="    Sign out", color = MaterialTheme.colorScheme.secondary, fontSize = 14.sp, modifier = Modifier.padding(bottom = 2.dp))
                }
            }
        }
    }
}

@Composable
fun TopBar(vm: ChatVM, navController: NavController) {
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
            ColorChangingCampusLogo(vm = vm,navController = navController)
        }
        Text(
            modifier = Modifier.padding(top = 6.dp),
            fontSize = 20.sp,
            text ="SETTINGS",
            color = MaterialTheme.colorScheme.primary
        )
    }
}