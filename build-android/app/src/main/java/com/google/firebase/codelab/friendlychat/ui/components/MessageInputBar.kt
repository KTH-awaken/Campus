@file:OptIn(ExperimentalMaterial3Api::class)

package com.google.firebase.codelab.friendlychat.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.campus.ui.viewmodels.ChatVM
import com.google.firebase.codelab.friendlychat.R

@Composable
fun MessageInputBar(vm: ChatVM) {
    var text by remember { mutableStateOf("") }
//    var text = vm.textInput.collectAsState(initial = "").value
    var isFocused by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { /* Handle attachment */ }) {
            Icon(
                painter = painterResource(id = R.drawable.add_photo_alternate_fill0_wght400_grad0_opsz24),
                contentDescription = "Attachment",
                tint = MaterialTheme.colorScheme.tertiary
            )
        }

        // Input field
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            placeholder = { Text("Message Campus Flemingsberg ") },//TODO: Change to Campus name"
        )

        IconButton(onClick = {
            if(text.isNotBlank()) {
                vm.sendMessage(text,vm.auth.currentUser!!.displayName!!, vm.auth.currentUser!!.photoUrl.toString())
                text = ""
            }
        }) {
            Icon(
                Icons.Default.Send, contentDescription = "Send",
                tint = MaterialTheme.colorScheme.tertiary
            )
        }
    }
}

@Composable
fun CustomInputField(vm: ChatVM) {
    var text by remember { mutableStateOf("") }
    var isFocused by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { /* Handle attachment */ }) {
            Icon(
                painter = painterResource(id = R.drawable.add_photo_alternate_fill0_wght400_grad0_opsz24),
                contentDescription = "Attachment",
                tint = MaterialTheme.colorScheme.tertiary
            )
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .height(28.dp)
                .background(
                    color = if (isFocused) Color.Transparent else Color.Gray,
                    shape = RoundedCornerShape(20.dp)
                )
                .border(
                    width = if (isFocused) 2.dp else 0.dp,
                    color = if (isFocused) Color.Blue else Color.Transparent,
                    shape = RoundedCornerShape(20.dp)
                )
                .clickable { isFocused = true }
                .padding(8.dp)
        ) {
            Text(text = text, color = Color.Black)
        }

        IconButton(onClick = {
            if (text.isNotBlank()) {
                vm.sendMessage(text, vm.auth.currentUser!!.displayName!!, vm.auth.currentUser!!.photoUrl.toString())
                text = ""
            }
        }) {
            Icon(
                Icons.Default.Send, contentDescription = "Send",
                tint = MaterialTheme.colorScheme.tertiary
            )
        }
    }
}
