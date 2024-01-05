package com.google.firebase.codelab.friendlychat.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.campus.ui.viewmodels.ChatVM

@Composable
fun StatusBar(vm: ChatVM) {
    ElevatedCard(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onBackground,),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp,),

        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp)
            .size(height = 62.dp, width = 100.dp),
    ){
        //lazy row of rooms
    }

}