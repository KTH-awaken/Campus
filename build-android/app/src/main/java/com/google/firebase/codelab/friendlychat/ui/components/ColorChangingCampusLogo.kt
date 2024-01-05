package com.example.campus.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.example.campus.ui.viewmodels.ChatVM
import com.google.firebase.codelab.friendlychat.R

@Composable
fun ColorChangingCampusLogo(vm: ChatVM,navController: NavController) {
    var darkMode = vm.darkMode.collectAsState().value

    if(darkMode){
        Image(
            painter = painterResource(id = R.drawable.campustextlogo2yellow),
            contentDescription = "Campus logo yellow",
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .fillMaxSize()
                .clickable{navController.navigate("home")}
        )
    }else {
        Image(
            painter = painterResource(id = R.drawable.campustextlogo2),
            contentDescription = "Campus logo black",
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .fillMaxSize()
                .clickable{navController.navigate("home")}

        )
    }
}